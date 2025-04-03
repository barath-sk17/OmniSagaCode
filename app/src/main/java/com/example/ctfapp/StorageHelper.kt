package com.example.ctfapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log

object StorageHelper {
    private const val PREF_NAME = "ctf_preferences"
    private const val FLAG_KEY = "hidden_flag_part"
    private const val TAG = "StorageHelper"

    fun storeFlagParts(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // First part of the flag encoded in Base64
        val part1 = Base64.encodeToString("FLAG{HIDDEN_".toByteArray(), Base64.DEFAULT)
        editor.putString(FLAG_KEY, part1)
        editor.apply()

        Log.d(TAG, "Stored encoded flag part: $part1")
    }

    fun retrieveFlagPart(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val retrievedData = sharedPreferences.getString(FLAG_KEY, "No data found")
        Log.d(TAG, "Retrieved flag part: $retrievedData")
        return retrievedData
    }
}