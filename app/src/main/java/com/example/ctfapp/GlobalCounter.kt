package com.example.ctfapp

import android.content.Context
import android.content.SharedPreferences

object GlobalCounter {
    var foundValues: Int = 0

    fun saveCounter(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences("CounterPrefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("foundValues", foundValues).apply()
    }

    fun loadCounter(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences("CounterPrefs", Context.MODE_PRIVATE)
        foundValues = prefs.getInt("foundValues", 0)
    }
}
