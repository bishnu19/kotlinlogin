package edu.msudenver.kotlinlogin

//package edu.msudenver.kotlinlogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.security.SecureRandom
import java.util.*


class SignUP : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        // Variables
        val username = findViewById<View>(R.id.username) as EditText
        val email = findViewById<View>(R.id.email) as EditText
        val password = findViewById<View>(R.id.password) as EditText
        val password2 = findViewById<View>(R.id.repassword) as EditText
        val regbtn = findViewById<View>(R.id.signupbtn) as MaterialButton

        regbtn.setOnClickListener { v ->

            // Screen holding variables and intent
            val username1 = username.text.toString()
            val emailAdress = email.text.toString()
            val passwordString1 = password.text.toString()
            val passwordString2 = password2.text.toString()
            val mainScreenIntent = Intent()
            mainScreenIntent.setClass(v.context, MainActivity::class.java)

            // Go back to login screen after accepting user credentials.
            if (passwordString1 == passwordString2)
            {
                Toast.makeText(
                    this@SignUP, "Welcome $username1", Toast.LENGTH_SHORT
                ).show()

                startActivity(mainScreenIntent)
            } // End if

            // If passwords don't match, let the user know.
            else
            {
                Toast.makeText(
                    this@SignUP,
                    "Passwords don't match.\nPlease try again.",
                    Toast.LENGTH_SHORT
                ).show()
            } // End else
        } // End onClick) // End regbtn.setOnClickListener
    } // End onCreate


    private val CHAR_POOL =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    private val STRING_LENGTH = 16
    private fun generateSalt(): String
    {
        val random: Random = SecureRandom()
        val stringBuilder = java.lang.StringBuilder()
        for (i in 0 until STRING_LENGTH)
        {
            val randomIndex = random.nextInt(CHAR_POOL.length)
            val randomChar = CHAR_POOL[randomIndex]
            stringBuilder.append(randomChar)
        }
        return stringBuilder.toString()
    }
} // End SignUp
