package com.example.ctfapp

import android.content.Context
import android.util.Base64
import android.util.Log
import android.content.SharedPreferences
import java.io.IOException

class MyObserver {
    companion object {
        fun validateFlag(context: Context, userFlag: String): Boolean {
            Log.d("MyObserver", "Validating flag")

            return try {
                Log.d("MyObserver", "Attempting to open flag file from assets...")
                val fileInputStream = context.assets.open("flag.txt")
                val encryptedFlag = fileInputStream.bufferedReader().use { it.readText().trim() }

                if (encryptedFlag.isEmpty()) {
                    Log.e("MyObserver", "Flag file is empty.")
                    return false
                }
                Log.d("MyObserver", "Encrypted flag read successfully: $encryptedFlag")

                val actualFlag = try {
                    String(Base64.decode(encryptedFlag, Base64.DEFAULT))
                } catch (e: IllegalArgumentException) {
                    Log.e("MyObserver", "Base64 decoding failed: ${e.message}")
                    return false
                }

                Log.d("MyObserver", "Decrypted flag: $actualFlag")
                Log.d("MyObserver", "User-provided flag: $userFlag")

                val isValid = userFlag == actualFlag
                Log.d("MyObserver", "Flag validation result: $isValid")
                isValid
            } catch (e: IOException) {
                Log.e("MyObserver", "Error accessing flag file: ${e.message}")
                false
            } catch (e: IllegalArgumentException) {
                Log.e("MyObserver", "Error decoding flag: ${e.message}")
                false
            }
        }

        fun checkForUpdates(context: Context): Boolean {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("FlagPrefs", Context.MODE_PRIVATE)
            val flagChanged = sharedPreferences.getBoolean("flagChanged", false)

            if (flagChanged) {
                sharedPreferences.edit().putBoolean("flagChanged", false).apply()
                return true
            }
            return false
        }

//        fun storeFlag(context: Context, flag: String) {
//            try {
//                val encryptedFlag = Base64.encodeToString(flag.toByteArray(), Base64.DEFAULT).trim()
//                context.openFileOutput("flag.txt", Context.MODE_PRIVATE).use {
//                    it.write(encryptedFlag.toByteArray())
//                }
//                Log.d("MyObserver", "Flag stored successfully.")
//            } catch (e: IOException) {
//                Log.e("MyObserver", "Error storing flag: ${e.message}")
//            }
//        }
    }
}


//        fun storeFlag(context: Context, flag: String) {
//            val file = File(context.filesDir, "flag.txt")
//            val encryptedFlag = Base64.encodeToString(flag.toByteArray(), Base64.DEFAULT).trim()
//            file.writeText(encryptedFlag)
//            Log.d("MyObserver", "Flag stored successfully.")
//        }

