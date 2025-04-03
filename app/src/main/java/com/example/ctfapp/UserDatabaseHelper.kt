package com.example.ctfapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath

    init {
        Log.d(TAG, "Initializing database helper")
        checkAndCopyDatabase(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
//        Log.d(TAG, "Creating users table")
//        val createTableQuery = """
//            CREATE TABLE users (
//                id INTEGER PRIMARY KEY AUTOINCREMENT,
//                username TEXT UNIQUE,
//                password TEXT
//            )
//        """.trimIndent()
//        db.execSQL(createTableQuery)
//
//        insertUser(db, "admin", "password123")  // Storing in plaintext for testing
//        Log.d(TAG, "Inserted default admin user")
        // insertUser(db, "sys_user", "CTF{ADVANCED_SQL_INJECTION}")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading database from version $oldVersion to $newVersion")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    private fun checkAndCopyDatabase(context: Context) {
        val dbFile = File(dbPath)

        if (!dbFile.exists()) {
            Log.d(TAG, "Database not found. Copying from assets...")
            copyDatabaseFromAssets(context)
        } else {
            Log.d(TAG, "Database already exists.")
        }
    }

    private fun copyDatabaseFromAssets(context: Context) {
        try {
            Log.d(TAG, "Copying database from assets")
            val inputStream: InputStream = context.assets.open(DATABASE_NAME)
            val outputFile = File(dbPath)
            val outputStream: OutputStream = FileOutputStream(outputFile)

            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            Log.d(TAG, "Database copied successfully from assets.")
        } catch (e: Exception) {
            Log.e(TAG, "Error copying database from assets", e)
        }
    }

    fun checkLogin(username: String, password: String): String? {
        val db = this.readableDatabase

        // **INSECURE: SQL Query Concatenation**
        val query = "SELECT password FROM users WHERE username = '$username' AND password = '$password'"
        Log.d(TAG, "Executing Query: $query")  // Debugging Output

        val cursor = db.rawQuery(query, null)

        var flag: String? = null
        if (cursor.moveToFirst()) {
            flag = cursor.getString(0)
            Log.d(TAG, "User found: $username")
        } else {
            Log.d(TAG, "User not found: $username")
        }
        cursor.close()
        return flag
    }

    private fun insertUser(db: SQLiteDatabase, username: String, password: String) {
        Log.d(TAG, "Inserting user: $username")
        val values = ContentValues().apply {
            put("username", username)
            put("password", password)
        }
        val rowId = db.insert("users", null, values)
        if (rowId == -1L) {
            Log.e(TAG, "Error inserting user: $username")
        } else {
            Log.d(TAG, "User inserted successfully: $username")
        }
    }

    companion object {
        private const val DATABASE_NAME = "insecure.db"
        private const val DATABASE_VERSION = 1
        private const val TAG = "UserDatabaseHelper"
    }
}