package com.example.ctfapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import android.util.Log
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class RetrieveDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_FLAGS (id INTEGER PRIMARY KEY, encryptedFlag TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FLAGS")
        onCreate(db)
    }

    // 🔐 **Encrypt and Store the Flag**
    fun storeFlag(flag: String) {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_FLAGS") // Ensure only one flag exists
        val encryptedFlag = encrypt(flag)
        val values = ContentValues().apply {
            put("encryptedFlag", encryptedFlag)
        }
        db.insert(TABLE_FLAGS, null, values)
        db.close()
        Log.d(TAG, "Encrypted flag stored in database.")
    }

    // 🔓 **Retrieve the Encrypted Flag**
    fun getEncryptedFlag(): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT encryptedFlag FROM $TABLE_FLAGS LIMIT 1", null)
        return if (cursor.moveToFirst()) {
            val encryptedFlag = cursor.getString(0)
            cursor.close()
            db.close()
            encryptedFlag
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // 🔥 **AES Encryption**
    private fun encrypt(input: String): String {
        val secretKey = SecretKeySpec(KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return Base64.encodeToString(cipher.doFinal(input.toByteArray()), Base64.DEFAULT)
    }

    // 🔥 **AES Decryption (Only Called in Memory)**
    fun decrypt(input: String): String {
        val secretKey = SecretKeySpec(KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return String(cipher.doFinal(Base64.decode(input, Base64.DEFAULT)))
    }

    companion object {
        private const val DATABASE_NAME = "flagdb"
        private const val DATABASE_VERSION = 1
        private const val TABLE_FLAGS = "flags"
        private const val TAG = "FlagDatabase"
        private const val KEY = "SuperSecretKey12" // 16-byte key
    }
}
