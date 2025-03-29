package com.example.ctfapp

import android.util.Log
import java.security.MessageDigest

object SecurityUtils {
    fun validateSecretKey(inputKey: String?): Boolean {
        val expectedHash = "5d41402abc4b2a76b9719d911017c592"
        val receivedHash = inputKey?.let { md5(it) }

        Log.d("SecurityUtils", "Expected Hash: $expectedHash")
        Log.d("SecurityUtils", "Received Hash: $receivedHash")

        return receivedHash == expectedHash
    }

    private fun md5(input: String): String {
        return MessageDigest.getInstance("MD5").digest(input.toByteArray()).joinToString("") {
            String.format("%02x", it)
        }
    }
}
