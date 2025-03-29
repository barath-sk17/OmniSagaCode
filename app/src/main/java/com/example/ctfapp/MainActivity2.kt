package com.example.ctfapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Method

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secret_manager)

        val flagInput: EditText = findViewById(R.id.flagInput)
        val checkButton: Button = findViewById(R.id.checkFlagButton)
        val showImageButton: Button = findViewById(R.id.showImageButton)
        val imageView: ImageView = findViewById(R.id.secretImage)

        // Hide the image button initially
        showImageButton.visibility = View.GONE
        imageView.visibility = View.GONE

        checkButton.setOnClickListener {
            val userFlag = flagInput.text.toString().trim()
            val actualFlag = getHiddenFlag()

            if (userFlag == actualFlag) {
                Toast.makeText(this, "✅ Correct Flag!", Toast.LENGTH_LONG).show()
                showImageButton.visibility = View.VISIBLE  // Show the button when the flag is correct
            } else {
                Toast.makeText(this, "❌ Incorrect Flag!", Toast.LENGTH_SHORT).show()
            }
        }

        showImageButton.setOnClickListener {
            imageView.visibility = View.VISIBLE  // Show the image when button is clicked
            imageView.setImageResource(R.drawable.shonen)  // Replace with your image resource
            Log.d("Shōnen (少年)", "Found in res/drawable")
        }
    }

    private fun getHiddenFlag(): String {
        return try {
            val secretClass = Class.forName("com.example.ctfapp.SecretManager")
            val method: Method = secretClass.getMethod("getSecret")
            method.invoke(null) as String
        } catch (e: Exception) {
            "ERROR"
        }
    }
}



//package com.example.ctfapp
//
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import java.lang.reflect.Method
//
//class MainActivity2 : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_secret_manager)
//
//        val flagInput: EditText = findViewById(R.id.flagInput)
//        val checkButton: Button = findViewById(R.id.checkFlagButton)
//
//        checkButton.setOnClickListener {
//            val userFlag = flagInput.text.toString().trim()
//            val actualFlag = getHiddenFlag()
//
//            if (userFlag == actualFlag) {
//                Toast.makeText(this, "✅ Correct Flag!", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(this, "❌ Incorrect Flag!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun getHiddenFlag(): String {
//        return try {
//            val secretClass = Class.forName("com.example.ctfapp.SecretManager")
//            val method: Method = secretClass.getMethod("getSecret")
//            method.invoke(null) as String
//        } catch (e: Exception) {
//            "ERROR"
//        }
//    }
//}
