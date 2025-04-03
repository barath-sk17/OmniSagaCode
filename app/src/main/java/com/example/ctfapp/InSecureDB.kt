package com.example.ctfapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class InSecureDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath

    init {
        Log.d(TAG, "Initializing InSecureDB")
        checkAndCopyDatabase(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating database table: secrets")
        db.execSQL("CREATE TABLE IF NOT EXISTS secrets (id INTEGER PRIMARY KEY, value TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading database from version $oldVersion to $newVersion")
        db.execSQL("DROP TABLE IF EXISTS secrets")
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
            Log.d(TAG, "Copying database from assets...")
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

    fun getFlagPart(): String {
        val db = this.readableDatabase
        Log.d(TAG, "Fetching flag part from database")
        val cursor: Cursor = db.rawQuery("SELECT value FROM secrets LIMIT 1", null)

        var flagPart = "NOT_FOUND"
        if (cursor.moveToFirst()) {
            flagPart = cursor.getString(0)
            Log.d(TAG, "Flag part retrieved: $flagPart")
        } else {
            Log.d(TAG, "No flag part found in the database")
        }
        cursor.close()
        db.close()
        return flagPart
    }

    companion object {
        private const val DATABASE_NAME = "ctf_hidden.db"
        private const val DATABASE_VERSION = 1
        private const val TAG = "InSecureDB"
    }
}




//package com.example.ctfapp
//
//import android.content.Context
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import android.util.Log
//import java.io.File
//import java.io.FileOutputStream
//import java.io.InputStream
//import java.io.OutputStream
//
//class InSecureDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    private val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath
//
//    init {
//        checkAndCopyDatabase(context)
//    }
//
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS secrets (id INTEGER PRIMARY KEY, value TEXT)")
//        db.execSQL("INSERT INTO secrets (value) VALUES ('MID_PART}')")
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS secrets")
//        onCreate(db)
//    }
//
//    private fun checkAndCopyDatabase(context: Context) {
//        val dbFile = File(dbPath)
//
//        if (!dbFile.exists()) {
//            Log.d("InSecureDB", "Database not found. Copying from assets...")
//            copyDatabaseFromAssets(context)
//        } else {
//            Log.d("InSecureDB", "Database already exists.")
//        }
//    }
//
//    private fun copyDatabaseFromAssets(context: Context) {
//        try {
//            val inputStream: InputStream = context.assets.open(DATABASE_NAME)
//            val outputFile = File(dbPath)
//            val outputStream: OutputStream = FileOutputStream(outputFile)
//
//            val buffer = ByteArray(1024)
//            var length: Int
//
//            while (inputStream.read(buffer).also { length = it } > 0) {
//                outputStream.write(buffer, 0, length)
//            }
//
//            outputStream.flush()
//            outputStream.close()
//            inputStream.close()
//
//            Log.d("InSecureDB", "Database copied successfully from assets.")
//        } catch (e: Exception) {
//            Log.e("InSecureDB", "Error copying database from assets", e)
//        }
//    }
//
//    fun getFlagPart(): String {
//        val db = this.readableDatabase
//        val cursor: Cursor = db.rawQuery("SELECT value FROM secrets LIMIT 1", null)
//
//        var flagPart = "NOT_FOUND"
//        if (cursor.moveToFirst()) {
//            flagPart = cursor.getString(0)
//        }
//        cursor.close()
//        db.close()
//        return flagPart
//    }
//
//    companion object {
//        private const val DATABASE_NAME = "ctf_hidden.db"
//        private const val DATABASE_VERSION = 1
//
//        fun storeFlagPart(context: Context) {
//            val db = InSecureDB(context).writableDatabase
//            db.execSQL("INSERT INTO secrets (value) VALUES ('SC0RE_H0W')")
//            db.close()
//        }
//    }
//}
