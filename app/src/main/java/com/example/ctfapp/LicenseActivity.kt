package com.example.ctfapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LicenseActivity : AppCompatActivity() {

    init {
        System.loadLibrary("license")
    }

    external fun checkLicense(): Boolean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)

        val statusText = findViewById<TextView>(R.id.license_status)

        // Perform license check
        val isLicenseValid = checkLicense()

        if (isLicenseValid) {
            Toast.makeText(this, "License Valid", Toast.LENGTH_LONG).show()
            val intent = Intent(this, CreditsActivity::class.java)  // Replace NextActivity with the desired Activity class
            startActivity(intent)
        } else {
            statusText.text = "Invalid License"
        }
    }
}
