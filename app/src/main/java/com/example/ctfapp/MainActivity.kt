package com.example.ctfapp
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val secretManagerButton: Button = findViewById(R.id.secretManagerButton)
        secretManagerButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        val startButton: Button = findViewById(R.id.VulnerableServiceButton)
        startButton.setOnClickListener {
            val intent = Intent(this, VulnerableServiceActivity::class.java)
            startActivity(intent)
        }

        val start1Button: Button = findViewById(R.id.DeeplinkHandlerButton)
        start1Button.setOnClickListener {
            val intent = Intent(this, DeeplinkHandlerActivity::class.java)
            startActivity(intent)
        }

        val InsecureButton: Button = findViewById(R.id.InsecureButton)
        InsecureButton.setOnClickListener({
            val intent = Intent(this, InsecureActivity::class.java)
            startActivity(intent)
        })

        val PsychoButton: Button = findViewById(R.id.PsychoButton)
        PsychoButton.setOnClickListener({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

    }
}
