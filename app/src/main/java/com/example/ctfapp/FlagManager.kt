package com.example.ctfapp

import android.content.Context
import android.util.Base64
import android.util.Log

object FlagManager {
    private val filenames = listOf(".hiddenA", "sysconfig.tmp", "logcache.dat", "cache_meta.bin")
    private const val encryptionKey = "ctfkey"

    fun retrieveFlag(context: Context): String {
        // Read all parts
        val parts = filenames.map { filename ->
            val data = FileUtils.readFromFile(context, filename)
            Log.d("FlagManager", "📄 Read from file $filename: $data")
            data
        }

        return try {
            // Decrypt parts
            val decryptedParts = parts.map { part ->
                // Decode Base64
                val decodedBytes = Base64.decode(part, Base64.NO_WRAP or Base64.URL_SAFE)

                // XOR decrypt
                xorEncryptDecrypt(decodedBytes, encryptionKey.toByteArray())
            }

            // Combine decrypted parts
            val decryptedFlag = decryptedParts.fold(byteArrayOf()) { acc, bytes ->
                acc + bytes
            }.toString(Charsets.UTF_8)

            Log.d("FlagManager", "🎉 Decrypted Flag: $decryptedFlag")
            decryptedFlag
        } catch (e: Exception) {
            Log.e("FlagManager", "❌ Error decrypting flag", e)
            "ERROR"
        }
    }

    // XOR encryption/decryption function
    private fun xorEncryptDecrypt(data: ByteArray, key: ByteArray): ByteArray {
        // Repeat the key to match data length
        val repeatedKey = ByteArray(data.size) { index ->
            key[index % key.size]
        }

        // XOR each byte
        return data.mapIndexed { index, byte ->
            (byte.toInt() xor repeatedKey[index].toInt()).toByte()
        }.toByteArray()
    }
}




//package com.example.ctfapp
//
//import android.content.Context
//import android.util.Base64
//import android.util.Log
//
//object FlagManager {
//    private val filenames = listOf(".hiddenA", "sysconfig.tmp", "logcache.dat", "cache_meta.bin")
//    private const val encryptionKey = "ctfkey"
//
//    fun initializeFlagStorage(context: Context) {
//        val flag = "CTF{INSECURE_DEEPLINKS_ARE_DANGEROUS}"
//
//        // Convert flag to bytes
//        val flagBytes = flag.toByteArray()
//
//        // Calculate length of each part
//        val partLength = flagBytes.size / 4
//
//        // Storage to hold encrypted parts
//        val storage = mutableMapOf<String, String>()
//
//        // Split and encrypt each part
//        for (i in 0 until 4) {
//            // Get the flag part
//            val start = i * partLength
//            val end = if (i < 3) start + partLength else flagBytes.size
//            val part = flagBytes.sliceArray(start until end)
//
//            // XOR encrypt the part
//            val encryptedPart = xorEncryptDecrypt(part, encryptionKey.toByteArray())
//
//            // Base64 encode the encrypted part
//            val encodedPart = Base64.encodeToString(encryptedPart, Base64.NO_WRAP or Base64.URL_SAFE)
//
//            // Write to file
//            FileUtils.writeToFile(context, filenames[i], encodedPart)
//            Log.d("FlagManager", "🔒 Encrypted Part ${i + 1}: $encodedPart")
//        }
//
//        Log.d("FlagManager", "✅ Flag storage initialized.")
//    }
//
//    fun retrieveFlag(context: Context): String {
//        // Read all parts
//        val parts = filenames.map { filename ->
//            val data = FileUtils.readFromFile(context, filename)
//            Log.d("FlagManager", "📄 Read from file $filename: $data")
//            data
//        }
//
//        return try {
//            // Decrypt parts
//            val decryptedParts = parts.map { part ->
//                // Decode Base64
//                val decodedBytes = Base64.decode(part, Base64.NO_WRAP or Base64.URL_SAFE)
//
//                // XOR decrypt
//                xorEncryptDecrypt(decodedBytes, encryptionKey.toByteArray())
//            }
//
//            // Combine decrypted parts
//            val decryptedFlag = decryptedParts.fold(byteArrayOf()) { acc, bytes ->
//                acc + bytes
//            }.toString(Charsets.UTF_8)
//
//            Log.d("FlagManager", "🎉 Decrypted Flag: $decryptedFlag")
//            decryptedFlag
//        } catch (e: Exception) {
//            Log.e("FlagManager", "❌ Error decrypting flag", e)
//            "ERROR"
//        }
//    }
//
//    // XOR encryption/decryption function
//    private fun xorEncryptDecrypt(data: ByteArray, key: ByteArray): ByteArray {
//        // Repeat the key to match data length
//        val repeatedKey = ByteArray(data.size) { index ->
//            key[index % key.size]
//        }
//
//        // XOR each byte
//        return data.mapIndexed { index, byte ->
//            (byte.toInt() xor repeatedKey[index].toInt()).toByte()
//        }.toByteArray()
//    }
//}