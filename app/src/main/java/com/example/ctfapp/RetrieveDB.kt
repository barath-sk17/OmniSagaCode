package com.example.ctfapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class RetrieveDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath

    init {
        checkAndCopyDatabase(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // No need to create a new DB, as we are copying it from assets
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FLAGS")
        onCreate(db)
    }

    /**
     * 🔍 **Check if the database exists, if not, copy it from assets**
     */
    private fun checkAndCopyDatabase(context: Context) {
        val dbFile = File(dbPath)

        if (!dbFile.exists()) {
            Log.d(TAG, "Database not found in $dbPath. Copying from assets...")
            copyDatabaseFromAssets(context)
        } else {
            Log.d(TAG, "Database already exists at $dbPath.")
        }
    }

    /**
     * 📂 **Copy database from assets folder**
     */
    private fun copyDatabaseFromAssets(context: Context) {
        try {
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

    // 🔥 **AES Decryption**
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
        private const val TAG = "RetrieveDB"
        private const val KEY = "SuperSecretKey12" // 16-byte key
    }
}
