fun storeFlag(context: Context, flag: String) {
            val file = File(context.filesDir, "flag.txt")
            val encryptedFlag = Base64.encodeToString(flag.toByteArray(), Base64.DEFAULT).trim()
            file.writeText(encryptedFlag)
            Log.d("MyObserver", "Flag stored successfully.")
        }