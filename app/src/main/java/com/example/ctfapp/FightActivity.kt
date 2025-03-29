package com.example.ctfapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class FightActivity : AppCompatActivity() {

    private lateinit var btnSelectEncodedImage: Button
    private lateinit var tvEncodedImagePath: TextView
    private lateinit var tvDecodedMessage: TextView

    private var encodedImageUri: Uri? = null
    private var decodedMessage: String? = null

    companion object {
        private const val PICK_ENCODED_IMAGE = 2
        const val DECODED_MESSAGE_EXTRA = "decoded_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fight)

        val flagInput: EditText = findViewById(R.id.flagInputMain)
        val checkFlagButton: Button = findViewById(R.id.checkFlagButtonMain)

        val dbHelper = SecureDB(this)

        initializeViews()
        setupListeners()

        // Decode Message directly in onCreate
        decodeMessage()

        checkFlagButton.setOnClickListener {
            //checkFlag()
            checkProcess(flagInput)
        }

    }

    private fun initializeViews() {
        btnSelectEncodedImage = findViewById(R.id.btnSelectEncodedImage)
        tvEncodedImagePath = findViewById(R.id.tvEncodedImagePath)
        tvDecodedMessage = findViewById(R.id.tvDecodedMessage)
    }

    private fun checkFlag(){
        Toast.makeText(this, "❌ The line is busy? Find another way!", Toast.LENGTH_SHORT).show()
    }

    private fun checkProcess(flagInput: EditText) {
        val enteredFlag = flagInput.text.toString()
        Log.d("Flag", "❌ Wrong flag entered: $enteredFlag")
        Log.d("Flag", "❌ Wrong flag entered: $decodedMessage")

        // Check if a message has been decoded and matches the entered flag
        if (decodedMessage != null && enteredFlag == decodedMessage) {
            Toast.makeText(this, "✅ Correct Flag!", Toast.LENGTH_SHORT).show()

            // Navigate to the next activity
            val intent = Intent(this, NextActivity::class.java)
            intent.putExtra(DECODED_MESSAGE_EXTRA, decodedMessage)
            startActivity(intent)
            finish() // Close this activity
        } else {
            Toast.makeText(this, "❌ Incorrect Flag!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        // Encoded Image Selection
        btnSelectEncodedImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_ENCODED_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_ENCODED_IMAGE -> {
                    encodedImageUri = data?.data
                    tvEncodedImagePath.text = encodedImageUri?.path ?: "Image selected"
                    // Automatically decode the selected image
                    decodeMessage()
                }
            }
        }
    }

    private fun decodeMessage() {
        if (encodedImageUri == null) {
            showToast("Please select an encoded image")
            return
        }

        try {
            val encodedBitmap = getBitmapFromUri(encodedImageUri!!)
            val binaryMessage = StringBuilder()

            for (y in 0 until encodedBitmap.height) {
                for (x in 0 until encodedBitmap.width) {
                    val pixel = encodedBitmap.getPixel(x, y)

                    val r = (pixel shr 16) and 0xff
                    val g = (pixel shr 8) and 0xff
                    val b = pixel and 0xff

                    binaryMessage.append(r and 1)
                    binaryMessage.append(g and 1)
                    binaryMessage.append(b and 1)

                    // Ensure we extract full characters before stopping
                    if (binaryMessage.length >= 456) {  // 160 bits = 20 characters * 8 bits
                        break
                    }
                }
                if (binaryMessage.length >= 456) break
            }

            decodedMessage = decodeBinaryToText(binaryMessage.toString())
            //tvDecodedMessage.text = decodedMessage?.take(80) ?: "Decoding failed"

        } catch (e: Exception) {
            showToast("Error decoding message: ${e.message}")
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        return contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        } ?: throw IOException("Cannot open image")
    }

    private fun decodeBinaryToText(binaryMessage: String): String {
        val result = StringBuilder()
        for (i in binaryMessage.indices step 8) {
            if (i + 8 > binaryMessage.length) break

            val byte = binaryMessage.substring(i, i + 8)
            val charCode = Integer.parseInt(byte, 2)
            Log.d("STEGNO", "🔍 Decoded URL: $charCode")
            result.append(charCode.toChar())
        }
        return result.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}