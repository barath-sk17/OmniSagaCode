package com.example.ctfapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val dbHelper = UserDatabaseHelper(this)
        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val flagTextView = findViewById<TextView>(R.id.flagTextView)
        val showImageButton = findViewById<Button>(R.id.showImageButton5)
        val secretImage = findViewById<ImageView>(R.id.secretImage5)

        // Initially hide the showImageButton
        showImageButton.visibility = View.GONE

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            Log.d("LoginActivity", "Attempting login with username: $username")

            val flag = dbHelper.checkLogin(username, password)
            if (flag != null) {
                Log.d("LoginActivity", "Login successful for username: $username")
                try {
                    val decryptedFlag = AESUtils.decrypt(flag)
                    flagTextView.text = "Flag: $decryptedFlag"
                    Log.d("LoginActivity", "Decrypted flag: $decryptedFlag")
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Error decrypting flag: ${e.message}", e)
                }
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                // Make the button visible after successful login
                showImageButton.visibility = View.VISIBLE
            } else {
                Log.w("LoginActivity", "Invalid login attempt for username: $username")
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        showImageButton.setOnClickListener {
            Log.d("LoginActivity", "Show Image button clicked")
            secretImage.visibility = View.VISIBLE
            Log.d("Psych&Thrill", "The .png file is located in the Debug directory under .cxx")
            Toast.makeText(this, "Revealing the secret image!", Toast.LENGTH_SHORT).show()
        }
    }
}
