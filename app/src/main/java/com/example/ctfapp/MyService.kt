import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.os.IBinder
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

class MyService : Service() {


    override fun onCreate() {
        super.onCreate()
        readFlagFile()
    }

    private fun readFlagFile() {
        try {
            val assetManager: AssetManager = assets
            val inputStream = assetManager.open("files/flag.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val flagContent = reader.use { it.readText() }

            Log.d("MyService", "Flag file content: $flagContent")
            notifyObserver(flagContent)

        } catch (e: Exception) {
            Log.e("MyService", "Error reading flag file from assets", e)
        }
    }

    private fun notifyObserver(flagContent: String) {
        val sharedPreferences: SharedPreferences = getSharedPreferences("FlagPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("flagContent", flagContent).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyService", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
