package com.example.ctfapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun checkLogin(username: String, password: String): String? {
        val db = this.readableDatabase
        val query = "SELECT password FROM users WHERE username = '$username' AND password = '$password'"
        Log.d("DatabaseQuery", "Executing query: $query")
        val cursor = db.rawQuery(query, null)
        var flag: String? = null
        if (cursor.moveToFirst()) {
            flag = cursor.getString(0)
        }
        cursor.close()
        return flag
    }

    companion object {
        private const val DATABASE_NAME = "vulnerable.db"
        private const val DATABASE_VERSION = 1
    }
}




//package com.example.ctfapp
//
//import android.content.ContentValues
//import android.content.Context
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//import android.util.Log
//
//class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    override fun onCreate(db: SQLiteDatabase) {
//        val createTableQuery = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)"
//        db.execSQL(createTableQuery)
//
//        val hiddenFlag = AESUtils.encrypt("CTF{ADVANCED_SQL_INJECTION}")
//        val values = ContentValues().apply {
//            put("username", "admin")
//            put("password", "password123")
//        }
//        db.insert("users", null, values)
//
//        val flagValues = ContentValues().apply {
//            put("username", "sys_user")
//            put("password", hiddenFlag)
//        }
//        db.insert("users", null, flagValues)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS users")
//        onCreate(db)
//    }
//
//    fun checkLogin(username: String, password: String): String? {
//        val db = this.readableDatabase
//        val query = "SELECT password FROM users WHERE username = '$username' AND password = '$password'"
//        Log.d("DatabaseQuery", "Executing query: $query")
//        val cursor = db.rawQuery(query, null)
//        var flag: String? = null
//        if (cursor.moveToFirst()) {
//            flag = cursor.getString(0)
//        }
//        cursor.close()
//        return flag
//    }
//
//    companion object {
//        private const val DATABASE_NAME = "vulnerable.db"
//        private const val DATABASE_VERSION = 1
//    }
//}
