package com.example.ctfapp

import android.content.Context
import android.util.Base64
import android.util.Log
import android.content.SharedPreferences
import java.io.File
import java.lang.ref.WeakReference

class MyObserver(context: Context) {
    private val contextRef = WeakReference(context)


    companion object {
        fun validateFlag(context: Context, userFlag: String): Boolean {
            Log.d("MyObserver", "Validating flag")

            val file = File(context.filesDir, "flag.txt")
            if (!file.exists()) {
                Log.e("MyObserver", "Flag file does not exist.")
                return false
            }

            val actualFlag = try {
                val encryptedFlag = file.readText().trim()
                if (encryptedFlag.isNotEmpty()) {
                    String(Base64.decode(encryptedFlag, Base64.DEFAULT))
                } else {
                    Log.e("MyObserver", "Flag file is empty.")
                    return false
                }
            } catch (e: Exception) {
                Log.e("MyObserver", "Error accessing flag file: ${e.message}")
                return false
            }

            return userFlag == actualFlag
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
//            val file = File(context.filesDir, "flag.txt")
//            val encryptedFlag = Base64.encodeToString(flag.toByteArray(), Base64.DEFAULT).trim()
//            file.writeText(encryptedFlag)
//            Log.d("MyObserver", "Flag stored successfully.")
//        }

    }
}
