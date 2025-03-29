package com.example.ctfapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.FileObserver
import android.os.IBinder
import android.util.Log
import java.io.File

class MyService : Service() {

    private var fileObserver: FileObserver? = null

    override fun onCreate() {
        super.onCreate()
        val flagFile = File(filesDir, "flag.txt")

        if (!flagFile.exists()) {
            Log.e("MyService", "Flag file does not exist. FileObserver not started.")
            return
        }

        fileObserver = object : FileObserver(flagFile.absolutePath, MODIFY) {
            override fun onEvent(event: Int, path: String?) {
                if (event == MODIFY) {
                    Log.d("MyService", "Flag file modified: $path")
                    notifyObserver()
                }
            }
        }

        fileObserver?.startWatching()
        Log.d("MyService", "Started watching flag file")
    }

    private fun notifyObserver() {
        val sharedPreferences = applicationContext.getSharedPreferences("FlagPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("flagChanged", true).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        fileObserver?.stopWatching()
        fileObserver = null
        Log.d("MyService", "Stopped watching flag file")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
