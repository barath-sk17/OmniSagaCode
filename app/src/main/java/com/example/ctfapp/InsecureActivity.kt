package com.example.ctfapp

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InsecureActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "InsecureActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insecure)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            Log.d(TAG, "Login attempt: Username = $username, Password = $password")

            if (username == "admin" && password == "password123") {
                Log.d(TAG, "Login successful. Redirecting to Dashboard.")

                // Simulating storing flag parts
                Log.d(TAG, "Storing flag parts in various locations")
                StorageHelper.storeFlagParts(this)
//                InSecureDB.storeFlagPart(this)
//                XorUtils.storeEncryptedFlag(this)

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                Log.w(TAG, "Invalid login attempt!")
                Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
