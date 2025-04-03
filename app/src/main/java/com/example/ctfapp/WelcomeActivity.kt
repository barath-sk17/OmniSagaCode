package com.example.ctfapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val enterButton: Button = findViewById(R.id.enterButton)
        enterButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val OmniSagaButton: ShapeableImageView  = findViewById(R.id.OmniSagaButton)
        OmniSagaButton.setOnClickListener {
            Toast.makeText(this, "Image Clicked!", Toast.LENGTH_SHORT).show()

            // Example: Navigate to another activity
            val intent = Intent(this, StegnoActivity::class.java)
            startActivity(intent)
        }

        val FightButton: ShapeableImageView  = findViewById(R.id.FightButton)
        FightButton.setOnClickListener {
            Toast.makeText(this, "Image Clicked!", Toast.LENGTH_SHORT).show()

            // Example: Navigate to another activity

            val intent = Intent(this, AmigoActivity::class.java)
//            val intent = Intent(this, FightActivity::class.java)
            startActivity(intent)
        }



//        val OmniSagaButton: Button = findViewById(R.id.OmniSagaButton)
//        OmniSagaButton.setOnClickListener {
//            val intent = Intent(this, StegnoActivity::class.java)
//            startActivity(intent)
//        }
    }
}
