//package com.example.ctfapp
//
//import android.content.Context
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//
//class InSecureDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    override fun onCreate(db: SQLiteDatabase) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS secrets (id INTEGER PRIMARY KEY, value TEXT)")
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("DROP TABLE IF EXISTS secrets")
//        onCreate(db)
//    }
//
//    // Function to retrieve the flag part from the database
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
//    }
//}



package com.example.ctfapp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class InSecureDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE secrets (id INTEGER PRIMARY KEY, value TEXT)")
        db.execSQL("INSERT INTO secrets (value) VALUES ('MID_PART}')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS secrets")
        onCreate(db)
    }

    // Store additional parts of the flag
    companion object {
        private const val DATABASE_NAME = "ctf_hidden.db"
        private const val DATABASE_VERSION = 1

        fun storeFlagPart(context: Context) {
            val db = InSecureDB(context).writableDatabase
            db.execSQL("INSERT INTO secrets (value) VALUES ('SC0RE_H0W')")
            db.close()
        }
    }

    // Function to retrieve the flag part from the database
    fun getFlagPart(): String {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT value FROM secrets LIMIT 1", null)

        var flagPart = "NOT_FOUND"
        if (cursor.moveToFirst()) {
            flagPart = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return flagPart
    }
}
