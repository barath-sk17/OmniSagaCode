package com.example.ctfapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import android.util.Log
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom

class SecureDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath

    init {
        checkAndCopyDatabase(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Do nothing, since we're copying from assets
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading database from version $oldVersion to $newVersion")

        db.execSQL("DROP TABLE IF EXISTS flags") // Delete old table
        onCreate(db) // Recreate table
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

    fun storeFlag(flag: String) {
        if (flag.isBlank()) {
            Log.e(TAG, "Error: Flag cannot be empty")
            return
        }

        val salt = generateSalt()
        val hashedFlag = hashFlag(flag, salt)

        Log.d(TAG, "Storing flag: hash=$hashedFlag, salt=${Base64.encodeToString(salt, Base64.NO_WRAP)}")

        val db = writableDatabase
        val stmt = db.compileStatement("INSERT INTO flags (hash, salt) VALUES (?, ?)")
        try {
            stmt.bindString(1, hashedFlag)
            stmt.bindString(2, Base64.encodeToString(salt, Base64.NO_WRAP))
            stmt.executeInsert()
            Log.d(TAG, "Flag stored successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error storing flag: ${e.message}")
        } finally {
            stmt.close()
            db.close()
        }
    }

    fun verifyFlag(userInput: String): Boolean {
        if (userInput.isBlank()) {
            Log.e(TAG, "Error: Input flag cannot be empty")
            return false
        }

        val db = readableDatabase
        val cursor = db.rawQuery("SELECT hash, salt FROM flags LIMIT 1", null)

        return try {
            if (cursor.moveToFirst()) {
                val storedHash = cursor.getString(0)
                val storedSalt = Base64.decode(cursor.getString(1), Base64.NO_WRAP)
                val computedHash = hashFlag(userInput, storedSalt)

                val result = computedHash == storedHash
                Log.d(TAG, "Flag verification result: $result")
                result
            } else {
                Log.e(TAG, "No hash found in database")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying flag: ${e.message}")
            false
        } finally {
            cursor.close()
            db.close()
        }
    }

    private fun generateSalt(): ByteArray {
        return ByteArray(16).apply {
            SecureRandom().nextBytes(this)
        }
    }

    private fun hashFlag(flag: String, salt: ByteArray): String {
        val params = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withSalt(salt)
            .withIterations(3)
            .withMemoryAsKB(65536)  // 64MB
            .withParallelism(4)
            .build()

        val generator = Argon2BytesGenerator()
        generator.init(params)

        val hash = ByteArray(32) // 32-byte hash output
        generator.generateBytes(flag.toByteArray(), hash, 0, hash.size)

        val hashedString = Base64.encodeToString(hash, Base64.NO_WRAP)
        Log.d(TAG, "Generated hash: $hashedString")
        return hashedString
    }

    companion object {
        private const val DATABASE_NAME = "secure_flags.db"
        private const val DATABASE_VERSION = 2  // Incremented version
        private const val TAG = "SecureDB"
    }
}
