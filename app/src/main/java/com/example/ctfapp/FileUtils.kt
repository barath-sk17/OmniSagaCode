package com.example.ctfapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class FileUtils(context: Context, private val dbName: String, private val tableName: String, private val columnName: String)
    : SQLiteOpenHelper(context, dbName, null, 1) {

    private val dbPath = context.getDatabasePath(dbName).absolutePath

    init {
        checkAndCopyDatabase(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS $tableName (id INTEGER PRIMARY KEY, $columnName TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    private fun checkAndCopyDatabase(context: Context) {
        val dbFile = File(dbPath)

        if (!dbFile.exists()) {
            Log.d("FileUtils", "Database not found. Copying from assets...")
            copyDatabaseFromAssets(context)
        } else {
            Log.d("FileUtils", "Database already exists.")
        }
    }

    private fun copyDatabaseFromAssets(context: Context) {
        try {
            val inputStream: InputStream = context.assets.open(dbName)
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

            Log.d("FileUtils", "Database copied successfully from assets.")
        } catch (e: Exception) {
            Log.e("FileUtils", "Error copying database from assets", e)
        }
    }

    fun insertFlagPart(part: String) {
        val db = writableDatabase
        db.execSQL("INSERT INTO $tableName ($columnName) VALUES (?)", arrayOf(part))
        db.close()
    }

    fun getFlagParts(): List<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $columnName FROM $tableName", null)
        val parts = mutableListOf<String>()

        while (cursor.moveToNext()) {
            parts.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return parts
    }

    fun clearTable() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $tableName")
        db.close()
    }
}


//object FileUtils {
//    fun writeToFile(context: Context, filename: String, data: String) {
//        try {
//            val file = File(context.filesDir, filename)
//            file.writeText(data)
//            Log.d("FileUtils", "✅ Successfully wrote to file: $filename")
//        } catch (e: Exception) {
//            Log.e("FileUtils", "❌ Error writing to file: $filename", e)
//        }
//    }
//
//    fun readFromFile(context: Context, filename: String): String {
//        return try {
//            context.assets.open(filename).bufferedReader().use { it.readText().trim() }
//        } catch (e: Exception) {
//            Log.e("FileUtils", "❌ Error reading file: $filename", e)
//            ""
//        }
//    }
//
//}
