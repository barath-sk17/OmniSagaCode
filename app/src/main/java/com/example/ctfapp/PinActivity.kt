package com.example.ctfapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast

class PinActivity : AppCompatActivity() {

    // Load the native library
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    private lateinit var pinInput: EditText
    private val securityManager = SecurityManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        pinInput = findViewById(R.id.pin_input)
    }

    fun onSubmit(view: android.view.View) {
        val enteredPin = pinInput.text.toString()

        // Check for FRIDA detection
        if (securityManager.isFridaDetected()) {
            Toast.makeText(this, "Security breach detected!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Validate the PIN
        if (securityManager.validatePIN(enteredPin)) {
            Toast.makeText(this, "Correct! Flag: CTF{you-cracked-it}", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LicenseActivity::class.java)  // Replace NextActivity with the desired Activity class
            startActivity(intent)
        } else {
            Toast.makeText(this, "Invalid PIN!", Toast.LENGTH_SHORT).show()
        }
    }
}
