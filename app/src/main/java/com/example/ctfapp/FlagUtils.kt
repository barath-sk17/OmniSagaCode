package com.example.ctfapp

import android.content.Context
import java.io.File

object FlagUtils {
    fun exportMisleadingData(context: Context) {
        val fakeHintFile = File(context.filesDir, "logs.txt")
        fakeHintFile.writeText("No secrets here... Maybe check network traffic? 😉")
    }
}
