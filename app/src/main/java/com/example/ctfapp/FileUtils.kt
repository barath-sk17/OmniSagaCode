package com.example.ctfapp

import android.content.Context
import android.util.Log
import java.io.File

object FileUtils {
    fun writeToFile(context: Context, filename: String, data: String) {
        try {
            val file = File(context.filesDir, filename)
            file.writeText(data)
            Log.d("FileUtils", "✅ Successfully wrote to file: $filename")
        } catch (e: Exception) {
            Log.e("FileUtils", "❌ Error writing to file: $filename", e)
        }
    }

    fun readFromFile(context: Context, filename: String): String {
        return try {
            val file = File(context.filesDir, filename)
            if (file.exists()) file.readText().trim() else ""
        } catch (e: Exception) {
            Log.e("FileUtils", "❌ Error reading file: $filename", e)
            ""
        }
    }
}
