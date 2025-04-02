package com.example.ctfapp

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.Charset

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("CTF", "🚀 DashboardActivity Started!")
        setContentView(R.layout.activity_dashboard)

        val fakeFlagText = findViewById<TextView>(R.id.flagTextView)
        val exportButton = findViewById<Button>(R.id.exportButton)
        val flagInput = findViewById<EditText>(R.id.flagInput3)
        val checkFlagButton = findViewById<Button>(R.id.checkFlagButton3)
        val hiddenButton = findViewById<Button>(R.id.showImageButton3)
        val secretImage = findViewById<ImageView>(R.id.secretImage3)

        hiddenButton.visibility = View.GONE
        secretImage.visibility = View.GONE

        Log.d("CTF", "🔍 Retrieving flag parts...")
        val dbHelper = InSecureDB(this)
        val encodedFlagPart1 = StorageHelper.retrieveFlagPart(this)
        val flagPart2 = dbHelper.getFlagPart()?.removeSuffix("}")
        val encryptedFlagPart3 = XorUtils.retrieveEncryptedFlag(this)

        Log.d("CTF", "📜 Encoded Flag Part 1: $encodedFlagPart1")
        Log.d("CTF", "📜 Flag Part 2: $flagPart2")
        Log.d("CTF", "📜 Encrypted Flag Part 3: $encryptedFlagPart3")

        val flagPart1 = try {
            encodedFlagPart1?.let {
                String(Base64.decode(it, Base64.DEFAULT), Charset.forName("UTF-8"))
            } ?: "ERROR"
        } catch (e: Exception) {
            Log.e("CTF", "⚠️ Error decoding Base64: ${e.message}")
            "ERROR"
        }

        val flagPart3 = encryptedFlagPart3?.let { xorDecrypt(it, "CTF") } ?: "ERROR"
        val flagPart4 = flagPart3?.let { xorDecrypt(it, "CTF") } ?: "ERROR"

        Log.d("CTF", "🔍 Decoded Flag Part 1: $flagPart1")
        Log.d("CTF", "🔍 Decrypted Flag Part 3: $flagPart4")

        val fullFlag = if (flagPart1 != "ERROR" && flagPart2 != null && flagPart4 != "ERROR") {
            "$flagPart1$flagPart2$flagPart4"
        } else {
            "INVALID_FLAG"
        }

        Log.d("CTF", "🔑 FULL FLAG Constructed: $fullFlag")

        fakeFlagText.text = "FLAG{FAKE_FLAG_FOR_CTF}"

        exportButton.setOnClickListener {
            Log.d("CTF", "📤 Exporting misleading data...")
            FlagUtils.exportMisleadingData(this)
        }

        checkFlagButton.setOnClickListener {
            val enteredFlag = flagInput.text.toString().trim()
            Log.d("CTF", "🔎 Checking entered flag: $enteredFlag")

            if (enteredFlag.isEmpty()) {
                Toast.makeText(this, "⚠️ Enter a flag!", Toast.LENGTH_SHORT).show()
                Log.w("CTF", "⚠️ No flag entered")
                return@setOnClickListener
            }

            if (enteredFlag == fullFlag) {
                Toast.makeText(this, "✅ Correct Flag!", Toast.LENGTH_SHORT).show()
                hiddenButton.visibility = View.VISIBLE
                Log.d("CTF", "✔ Secret Button Unlocked!")
            } else {
                Toast.makeText(this, "❌ Incorrect Flag. Try again!", Toast.LENGTH_SHORT).show()
                Log.w("CTF", "❌ Wrong flag entered: $enteredFlag")
            }
        }

        hiddenButton.setOnClickListener {
            secretImage.visibility = View.VISIBLE
            Log.d("Slice of Life", "Paths hide in names; words unlock, not code.")
            Toast.makeText(this, "🔓 Secret Image Revealed!", Toast.LENGTH_SHORT).show()
            Log.d("CTF", "🔓 Secret Image Unlocked!")
        }
    }

    private fun xorDecrypt(encryptedStr: String, key: String): String {
        Log.d("CTF", "🔑 Performing XOR Decryption...")
        return encryptedStr.split(" ")
            .mapIndexed { i, num -> (num.toInt() xor key[i % key.length].code).toChar() }
            .joinToString("").also {
                Log.d("CTF", "🔓 Decryption Result: $it")
            }
    }
}
