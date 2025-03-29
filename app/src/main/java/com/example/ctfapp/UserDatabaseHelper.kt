package com.example.ctfapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                username TEXT UNIQUE, 
                password TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)

        insertUser(db, "admin", "password123")  // Storing in plaintext for testing
        //insertUser(db, "sys_user", "CTF{ADVANCED_SQL_INJECTION}")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun checkLogin(username: String, password: String): String? {
        val db = this.readableDatabase

        // **INSECURE: SQL Query Concatenation**
        val query = "SELECT password FROM users WHERE username = '$username' AND password = '$password'"
        Log.d("SQL_QUERY", "Executing Query: $query")  // Debugging Output

        val cursor = db.rawQuery(query, null)

        var flag: String? = null
        if (cursor.moveToFirst()) {
            flag = cursor.getString(0)
        }
        cursor.close()
        return flag
    }

    private fun insertUser(db: SQLiteDatabase, username: String, password: String) {
        val values = ContentValues().apply {
            put("username", username)
            put("password", password)
        }
        db.insert("users", null, values)
    }

    companion object {
        private const val DATABASE_NAME = "insecure.db"
        private const val DATABASE_VERSION = 1
    }
}
