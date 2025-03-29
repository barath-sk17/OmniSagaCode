package com.example.ctfapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DeeplinkHandlerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deeplink_handler)

        val flagTextView: TextView = findViewById(R.id.flagTextView)
        val flagInput: EditText = findViewById(R.id.flagInput3)
        val checkFlagButton: Button = findViewById(R.id.checkFlagButton3)
        val showImageButton: Button = findViewById(R.id.showImageButton3)
        val secretImage: ImageView = findViewById(R.id.secretImage3)

        // Initially hide elements
        checkFlagButton.visibility = View.GONE
        showImageButton.visibility = View.GONE
        secretImage.visibility = View.GONE

        // Process deep link data
        intent.dataString?.let { fullUriString ->
            Log.d("DeeplinkHandler", "🔍 Full URI String Raw: $fullUriString")
            val intentData = Uri.parse(fullUriString)
            processDeepLink(intentData, flagTextView, checkFlagButton, flagInput)
        } ?: run {
            flagTextView.text = "No deep link data found."
        }

        // When "Check Flag" is clicked, validate input
        checkFlagButton.setOnClickListener {
            val enteredFlag = flagInput.text.toString().trim()
            val correctFlag = FlagManager.retrieveFlag(this)

            if (enteredFlag == correctFlag) {
                Toast.makeText(this, "✅ Correct Flag!", Toast.LENGTH_SHORT).show()
                showImageButton.visibility = View.VISIBLE  // Reveal "Show Image" button
            } else {
                Toast.makeText(this, "❌ Incorrect Flag!", Toast.LENGTH_SHORT).show()
            }
        }

        // When "Show Image" button is clicked, reveal secret image
        showImageButton.setOnClickListener {
            secretImage.visibility = View.VISIBLE
            Toast.makeText(this, "🎉 Secret Image Revealed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun processDeepLink(
        intentData: Uri,
        flagTextView: TextView,
        checkFlagButton: Button,
        flagInput: EditText
    ) {
        val decodedUrl = Uri.decode(intentData.toString())
        Log.d("DeeplinkHandler", "🔍 Decoded URL: $decodedUrl")

        val decodedUri = Uri.parse(decodedUrl)
        val queryString = decodedUri.query ?: ""

        val queryParams = queryString.split("&").mapNotNull { param ->
            val parts = param.split("=")
            if (parts.size == 2) parts[0] to Uri.decode(parts[1]) else null
        }.toMap()

        val actionParam = queryParams["action"]
        val secretKey = queryParams["key"]
        //FlagManager.initializeFlagStorage(this)
        if (actionParam == "getFlag" && SecurityUtils.validateSecretKey(secretKey)) {
            val flag = FlagManager.retrieveFlag(this)
            flagTextView.text = flag  // Show the flag
            checkFlagButton.visibility = View.VISIBLE  // Show "Check Flag" button

            Log.d("DeeplinkHandler", "🎉 Flag retrieved successfully!")
        } else {
            flagTextView.text = "Unauthorized access attempt detected!"
            Log.d("Seinen (青年)", "Paths hide in names; words unlock, not code.")
        }
    }
}





//package com.example.ctfapp
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//
//class DeeplinkHandlerActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_deeplink_handler)
//
//        val flagTextView: TextView = findViewById(R.id.flagTextView)
//
//        // Check if this is a deep link or direct launch
//        val intent = intent
//        val intentData: Uri? = intent?.data
//        val action = intent?.action
//
//        Log.d("DeeplinkHandler", "🔍 Received Intent: $intent")
//        Log.d("DeeplinkHandler", "🔍 Intent Action: $action")
//
//        if (action == Intent.ACTION_VIEW && intentData != null) {
//            // This is a deep link
//            processDeepLink(intentData, flagTextView)
//        } else {
//            // This is a direct launch - show test UI
//            setupTestInterface(flagTextView)
//        }
//    }
//
//    private fun processDeepLink(intentData: Uri, flagTextView: TextView) {
//        val schemeSpecificPart = intentData.toString()
//        Log.d("DeeplinkHandler", "🔍 Full URI: $schemeSpecificPart")
//
//        // Parse URL parameters manually to handle various deep link formats
//        val queryParams = mutableMapOf<String, String>()
//
//        // Check if there's a query string
//        val queryIndex = schemeSpecificPart.indexOf('?')
//        if (queryIndex != -1 && queryIndex < schemeSpecificPart.length - 1) {
//            val queryString = schemeSpecificPart.substring(queryIndex + 1)
//            Log.d("DeeplinkHandler", "🔍 Query String: $queryString")
//
//            val pairs = queryString.split("&")
//            for (pair in pairs) {
//                val idx = pair.indexOf("=")
//                if (idx > 0) {
//                    val key = pair.substring(0, idx)
//                    val value = if (idx < pair.length - 1) pair.substring(idx + 1) else ""
//                    queryParams[key] = value
//                }
//            }
//        }
//
//        // Extract parameters we need
//        val actionParam = queryParams["action"]
//        val secretKey = queryParams["key"]
//
//        Log.d("DeeplinkHandler", "🔍 Extracted - Action: $actionParam, Key: $secretKey")
//
//        if (actionParam == "getFlag" && validateSecretKey(secretKey)) {
//            flagTextView.text = FlagManager.retrieveFlag(this)
//            Log.d("DeeplinkHandler", "🎉 Flag retrieved successfully!")
//        } else if (actionParam == "getFlag") {
//            flagTextView.text = "Unauthorized access attempt detected!"
//            Log.d("DeeplinkHandler", "❌ Authentication failed - Invalid key")
//        } else {
//            flagTextView.text = "Invalid or missing action parameter!"
//            Log.d("DeeplinkHandler", "❌ Invalid action parameter: $actionParam")
//        }
//    }
//
//    private fun validateSecretKey(inputKey: String?): Boolean {
//        val expectedHash = "5d41402abc4b2a76b9719d911017c592"
//        val receivedHash = inputKey?.let { md5(it) }
//
//        Log.d("DeeplinkHandler", "Expected Hash: $expectedHash")
//        Log.d("DeeplinkHandler", "Received Hash: $receivedHash")
//
//        return receivedHash == expectedHash
//    }
//
//    private fun md5(input: String): String {
//        return java.security.MessageDigest.getInstance("MD5").digest(input.toByteArray()).joinToString("") {
//            String.format("%02x", it)
//        }
//    }
//
//    private fun setupTestInterface(flagTextView: TextView) {
//        // Add a test button to the UI programmatically
//        try {
//            val testButton = Button(this)
//            testButton.text = "Test Deep Link"
//            testButton.setOnClickListener {
//                // Create a test deep link with the correct key ("hello")
//                val testUri = Uri.parse("ctfapp://yourapp?action=getFlag&key=hello")
//
//                // Create and start the intent
//                val testIntent = Intent(Intent.ACTION_VIEW, testUri)
//                testIntent.setPackage(packageName)
//                startActivity(testIntent)
//            }
//
//            // Add to the layout (assuming you're using a LinearLayout or similar)
//            val parent = flagTextView.parent as android.view.ViewGroup
//            parent.addView(testButton)
//
//            flagTextView.text = "Deep link test mode. Use the button below to test."
//            Log.d("DeeplinkHandler", "ℹ️ Test interface set up")
//        } catch (e: Exception) {
//            Log.e("DeeplinkHandler", "Error setting up test interface", e)
//            flagTextView.text = "App launched directly. To test deep link:\n\nctfapp://yourapp?action=getFlag&key=hello"
//        }
//    }
//}