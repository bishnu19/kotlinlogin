package edu.msudenver.kotlinlogin

import java.security.*
import java.util.*


class UserDatabase
{
    private val users = mutableListOf<User>()

    data class User(
        val username: String,
        val email: String,
        val passwordHash: String,
        val salt: String
    )

    fun addUser(username: String, email: String, password: String, salt: String)
    {
        if (userExists(username)) throw IllegalArgumentException("Username already exists")

        val passwordHash = hashPassword(password, salt)
        users.add(User(username, email, passwordHash, salt))
    }

    fun getUser(username: String): User?
    {
        return users.find { it.username == username }
    }


    fun userExists(username: String): Boolean
    {
        return users.any { it.username == username }
    }

    fun hashPassword(password: String, salt: String): String
    {
        val digest = hashString("SHA-256", password + salt)
        return Base64.getEncoder().encodeToString(digest)
    }

    fun checkPassword(username: String, password: String): Boolean
    {
        val user = getUser(username) ?: return false
        val hashedPassword = hashPassword(password, user.salt)
        return hashedPassword == user.passwordHash
    }

    private fun hashString(type: String, input: String): ByteArray
    {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest.getInstance(type).digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString().toByteArray()
    }

}
