package com.example.ctfapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException

class StegnoActivity : AppCompatActivity() {

    private lateinit var btnSelectEncodedImage: Button
    private lateinit var btnDecodeMessage: Button
    private lateinit var tvEncodedImagePath: TextView
    private lateinit var tvDecodedMessage: TextView

    private var encodedImageUri: Uri? = null

    companion object {
        private const val PICK_ENCODED_IMAGE = 2
        private const val TAG = "StegnoActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stegno)
        Log.d(TAG, "onCreate: Activity initialized")

        val flagInput: EditText = findViewById(R.id.flagInputMain)
        val checkFlagButton: Button = findViewById(R.id.checkFlagButtonMain)
        val showImageButton: Button = findViewById(R.id.showImageButtonMain)
        val secretImage: ImageView = findViewById(R.id.secretImageMain)

        val dbHelper = SecureDB(this)
        Log.d(TAG, "onCreate: SecureDB initialized")

        showImageButton.visibility = View.GONE
        secretImage.visibility = View.GONE

        initializeViews()
        setupListeners()

        checkFlagButton.setOnClickListener {
            val enteredFlag = flagInput.text.toString()
            Log.d(TAG, "checkFlagButton: Entered flag: $enteredFlag")

            if (dbHelper.verifyFlag(enteredFlag)) {
                Toast.
                makeText(this, "✅ Correct Flag!", Toast.LENGTH_SHORT).show()
                showImageButton.visibility = View.VISIBLE
                Log.d(TAG, "checkFlagButton: Correct flag entered")
            } else {
                Toast.makeText(this, "❌ Incorrect Flag!", Toast.LENGTH_SHORT).show()
                showImageButton.visibility = View.GONE
                Log.d(TAG, "checkFlagButton: Incorrect flag entered")
            }
        }

        showImageButton.setOnClickListener {
            secretImage.visibility = View.VISIBLE
            Log.d("OGF", "If you’ve ever been stuck in a loop, bouncing between failures, you might just call it… buggy.")
            Toast.makeText(this, "🎉 Secret Image Revealed!", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "showImageButton: Secret image revealed")
        }
    }

    private fun initializeViews() {
        Log.d(TAG, "initializeViews: Initializing UI components")
        btnSelectEncodedImage = findViewById(R.id.btnSelectEncodedImage)
        btnDecodeMessage = findViewById(R.id.btnDecodeMessage)
        tvEncodedImagePath = findViewById(R.id.tvEncodedImagePath)
        tvDecodedMessage = findViewById(R.id.tvDecodedMessage)
    }

    private fun setupListeners() {
        Log.d(TAG, "setupListeners: Setting up listeners")
        btnSelectEncodedImage.setOnClickListener {
            Log.d(TAG, "btnSelectEncodedImage: Image selection started")
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_ENCODED_IMAGE)
        }

        btnDecodeMessage.setOnClickListener {
            Log.d(TAG, "btnDecodeMessage: Decoding process started")
            decodeMessage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: Request Code: $requestCode, Result Code: $resultCode")

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_ENCODED_IMAGE -> {
                    encodedImageUri = data?.data
                    tvEncodedImagePath.text = encodedImageUri?.path ?: "Image selected"
                    Log.d(TAG, "onActivityResult: Encoded image selected: ${encodedImageUri?.path}")
                }
            }
        }
    }

    private fun decodeMessage() {
        if (encodedImageUri == null) {
            showToast("Please select an encoded image")
            Log.w(TAG, "decodeMessage: No image selected")
            return
        }

        try {
            val encodedBitmap = getBitmapFromUri(encodedImageUri!!)
            Log.d(TAG, "decodeMessage: Image loaded successfully")
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

                    if (binaryMessage.length >= 640) {
                        break
                    }
                }
                if (binaryMessage.length >= 640) break
            }

            val decodedMessage = decodeBinaryToText(binaryMessage.toString())
            tvDecodedMessage.text = decodedMessage.take(80)
            Log.d(TAG, "decodeMessage: Decoded message: ${decodedMessage.take(80)}")
        } catch (e: Exception) {
            Log.e(TAG, "decodeMessage: Error decoding message", e)
            showToast("Error decoding message: ${e.message}")
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        Log.d(TAG, "getBitmapFromUri: Loading bitmap from URI")
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
            Log.d(TAG, "decodeBinaryToText: Decoded char code: $charCode")
            result.append(charCode.toChar())
        }
        return result.toString()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
