package com.example.ctfapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.File
import java.lang.ref.WeakReference

class VulnerableServiceActivity : AppCompatActivity() {
    private lateinit var inputField: EditText
    private lateinit var validateButton: Button
    private lateinit var showImageButton: Button
    private lateinit var imageView: ImageView
    private lateinit var handlerThread: HandlerThread
    private lateinit var backgroundHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vulnerable_service)

        inputField = findViewById(R.id.inputField)
        validateButton = findViewById(R.id.validateButton)
        showImageButton = findViewById(R.id.showImageButton)
        imageView = findViewById(R.id.secretImage)

        showImageButton.visibility = View.GONE
        imageView.visibility = View.GONE

//        val file = File(filesDir, "flag.txt")
//        if (!file.exists()) {
//            MyObserver.storeFlag(this, "SECRET_FLAG_UIBASECRET")
//            Log.d("VulnerableServiceActivity", "Flag stored for the first time.")
//        }


        validateButton.setOnClickListener {
            val userFlag = inputField.text.toString().trim()
            if (MyObserver.validateFlag(this, userFlag)) {
                Toast.makeText(this, "✅ Correct Flag!", Toast.LENGTH_LONG).show()
                showImageButton.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "❌ Incorrect Flag!", Toast.LENGTH_SHORT).show()
            }
        }

        // Start service safely
        val serviceIntent = Intent(applicationContext, MyService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            applicationContext.startForegroundService(serviceIntent)
        } else {
            applicationContext.startService(serviceIntent)
        }

        // Background handler for memory efficiency
        handlerThread = HandlerThread("BackgroundThread").apply { start() }
        backgroundHandler = Handler(handlerThread.looper)

        backgroundHandler.post(object : Runnable {
            override fun run() {
                if (MyObserver.checkForUpdates(this@VulnerableServiceActivity)) {
                    runOnUiThread {
                        Toast.makeText(this@VulnerableServiceActivity, "⚡ Flag file changed!", Toast.LENGTH_SHORT).show()
                    }
                }
                backgroundHandler.postDelayed(this, 5000)
            }
        })

        showImageButton.setOnClickListener {
            imageView.visibility = View.VISIBLE
            Glide.with(this).load(R.drawable.isekai).into(imageView)
            Log.d("Isekai (異世界)", "SQLite and binary coexist where secrets rest. What if an image wasn’t where you expected it to be?")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerThread.quitSafely()
    }
}
