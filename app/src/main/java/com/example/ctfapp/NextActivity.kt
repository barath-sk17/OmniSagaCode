package com.example.ctfapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.security.SecureRandom

class NextActivity : AppCompatActivity() {

    private lateinit var db: RetrieveDB
    private val TAG = "CTF_CHALLENGE"
    private var decryptedFlag: String? = null // Store the flag securely

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next) // ✅ Ensures UI is displayed

        db = RetrieveDB(this) // Initialize database

        val checkSecurityButton = findViewById<Button>(R.id.checkSecurityButton)
        val statusText = findViewById<TextView>(R.id.statusText)
        val proceedButton = findViewById<Button>(R.id.proceedButton)

        checkSecurityButton.setOnClickListener {
            if (isDebuggerAttached() || isDeviceRooted()) {
                statusText.text = "Security Threat Detected! ❌"
                Log.e(TAG, "Security Threat Detected!")
            } else {
                statusText.text = "System Secure ✅"
                Log.i(TAG, "System Secure ✅")
            }
        }

        // Generate and store the encrypted flag
        val generatedFlag = generateFlag()
        db.storeFlag(generatedFlag)

        // Retrieve encrypted flag from DB
        val encryptedFlag = db.getEncryptedFlag()
        if (encryptedFlag != null) {
            decryptedFlag = db.decrypt(encryptedFlag)

            // Prevent moving to the next activity unless the flag is extracted dynamically
            proceedButton.setOnClickListener {
                if (isFlagExtracted()) {
                    secureFunction(decryptedFlag!!)
                } else {
                    Log.w(TAG, "Attempt to proceed without extracting flag!")
                }
            }
        }
    }

    // 🔥 **Generate a Flag**
    private fun generateFlag(): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val secureRandom = SecureRandom()
        val prefix = "CTF{"
        val suffix = "}"
        val flagBody = (1..10)
            .map { charset[secureRandom.nextInt(charset.length)] }
            .joinToString("")
        return prefix + flagBody + suffix
    }

    // ✅ **Ensure flag is extracted dynamically before moving to next activity**
    private fun isFlagExtracted(): Boolean {
        return decryptedFlag?.contains("CTF{") == true // Ensure the flag is retrieved
    }

    // 🚀 **Only proceed if flag is extracted dynamically**
    private fun secureFunction(flag: String) {
        Log.d(TAG, "Secure function executed with flag: $flag") // Fake log to mislead

        val intent = Intent(this, PinActivity::class.java)
        startActivity(intent)
        finish()
    }

    // 🔒 **Anti-Debugging Check**
    private fun isDebuggerAttached(): Boolean {
        return android.os.Debug.isDebuggerConnected() || android.os.Debug.waitingForDebugger()
    }

    // 🔥 **Root Detection**
    private fun isDeviceRooted(): Boolean {
        val rootPaths = arrayOf(
            "/system/bin/su", "/system/xbin/su", "/sbin/su",
            "/system/app/Superuser.apk", "/system/app/Magisk.apk"
        )
        return rootPaths.any { java.io.File(it).exists() }
    }
}
