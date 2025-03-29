package com.example.ctfapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64

object StorageHelper {
    private const val PREF_NAME = "ctf_preferences"
    private const val FLAG_KEY = "hidden_flag_part"

    fun storeFlagParts(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // First part of the flag encoded in Base64
        val part1 = Base64.encodeToString("FLAG{HIDDEN_".toByteArray(), Base64.DEFAULT)
        editor.putString(FLAG_KEY, part1)
        editor.apply()
    }

    fun retrieveFlagPart(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(FLAG_KEY, "No data found")
    }
}
