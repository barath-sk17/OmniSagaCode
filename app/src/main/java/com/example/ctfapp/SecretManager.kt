package com.example.ctfapp

import android.util.Base64

object SecretManager {
    init {
        System.loadLibrary("getme") // Load the native library
    }

    external fun getEncodedSecret(): String

    private fun xorDecrypt(input: String, key: String): String {

        val hexBytes = input.split(" ")
        val chunkSize = 4
        val reversedHex = StringBuilder()

        for (i in hexBytes.indices step chunkSize) {
            for (j in (i + chunkSize - 1).coerceAtMost(hexBytes.lastIndex) downTo i) {
                reversedHex.append(hexBytes[j]).append(" ")
            }
        }

        val reversedHexString = reversedHex.toString().trim()

        try {
            val bytes = hexStringToByteArray(reversedHexString)
            val result = bytes.toString(Charsets.UTF_8)
            println("Decoded String: $result")
            val decodedBytes = Base64.decode(result, Base64.DEFAULT)
            return decodedBytes.mapIndexed { i, byte ->
                (byte.toInt() xor key[i % key.length].code).toChar()
            }.joinToString("")
        } catch (e: Exception) {
            println("Error decoding hex: ${e.message}")
        }
        return ""
    }


    fun hexStringToByteArray(hex: String): ByteArray {
        val hexArray = hex.split(" ")
        return ByteArray(hexArray.size) { hexArray[it].toInt(16).toByte() }
    }

    @JvmStatic
    fun getSecret(): String {
        val encodedSecret = getEncodedSecret()
        println(encodedSecret)// 🔥 Retrieve from C++
        val xorKey = "cd7862r=="  // You can also retrieve this from C++ for extra security
        return xorDecrypt(encodedSecret, xorKey)
    }
}
