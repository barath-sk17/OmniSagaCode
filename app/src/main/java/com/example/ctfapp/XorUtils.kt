package com.example.ctfapp

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

object XorUtils {
    private const val KEY = "CTF"
    private const val TAG = "XorUtils"

    fun retrieveEncryptedFlag(context: Context): String? {
        return try {
            Log.d(TAG, "Opening hidden_flag2.txt from assets")
            val inputStream = context.assets.open("hidden_flag2.txt")
            val encryptedText = inputStream.bufferedReader().use(BufferedReader::readText)
            Log.d(TAG, "Encrypted flag retrieved: $encryptedText")
            xorEncrypt(encryptedText, KEY)
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving encrypted flag", e)
            null
        }
    }

    private fun xorEncrypt(input: String, key: String): String {
        Log.d(TAG, "Decrypting flag using XOR")
        return input.split(" ").mapIndexed { i, s ->
            (s.toInt() xor key[i % key.length].code).toChar()
        }.joinToString("")
    }
}


//package com.example.ctfapp
//
//import android.content.Context
//import java.io.File
//
//object XorUtils {
//    private const val KEY = "CTF"
//
//    fun storeEncryptedFlag(context: Context) {
//        val file = File(context.filesDir, "hidden_flag2.txt")
//        val encryptedPart = xorEncrypt("_GOOD_JOB}", KEY)
//        file.writeText(encryptedPart)
//    }
//
//    fun retrieveEncryptedFlag(context: Context): String? {
//        val file = File(context.filesDir, "hidden_flag2.txt")
//        return if (file.exists()) xorEncrypt(file.readText(), KEY) else null
//    }
//
//    private fun xorEncrypt(input: String, key: String): String {
//        return input.mapIndexed { i, c -> c.code xor key[i % key.length].code }
//            .joinToString(" ") { it.toString() }
//    }
//}

