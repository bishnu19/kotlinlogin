package edu.msudenver.kotlinlogin

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.Serializable

class BikeDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    Serializable {

    companion object {
        const val DATABASE_NAME = "Bike.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        //TODO("Not yet implemented")
        // create the table
        p0?.execSQL("""
            CREATE TABLE bikes (
                year    TEXT PRIMARY KEY, 
                make    TEXT NOT NULL, 
                model    TEXT NOT NULL,
                size    TEXT NOT NULL)
        """)

        // populate the table with a few items
        p0?.execSQL("""
            INSERT INTO bikes VALUES 
                ("2020","Honda",  "H20", "25")
                
        """)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        //TODO("Not yet implemented")
        // drop the table
        p0?.execSQL("""
            DROP TABLE IF EXISTS bikes
        """)

        // then call "onCreate" again
        onCreate(p0)
    }


}