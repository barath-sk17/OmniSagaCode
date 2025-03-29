package com.example.ctfapp

import android.content.Context
import java.io.File

object XorUtils {
    private const val KEY = "CTF"

    fun storeEncryptedFlag(context: Context) {
        val file = File(context.filesDir, "hidden_flag2.txt")
        val encryptedPart = xorEncrypt("_GOOD_JOB}", KEY)
        file.writeText(encryptedPart)
    }

    fun retrieveEncryptedFlag(context: Context): String? {
        val file = File(context.filesDir, "hidden_flag2.txt")
        return if (file.exists()) xorEncrypt(file.readText(), KEY) else null
    }

    private fun xorEncrypt(input: String, key: String): String {
        return input.mapIndexed { i, c -> c.code xor key[i % key.length].code }
            .joinToString(" ") { it.toString() }
    }
}
