package com.example.ctfapp
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.security.SecureRandom

object AESUtils {
    private const val SECRET_KEY = "my_super_secret_key"
    private const val SALT = "my_salt_value"

    fun encrypt(input: String): String {
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getKey(), IvParameterSpec(iv))
        val encrypted = cipher.doFinal(input.toByteArray())
        return Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
    }

    fun decrypt(input: String): String {
        val decoded = Base64.decode(input, Base64.DEFAULT)
        val iv = decoded.copyOfRange(0, 16)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        val decrypted = cipher.doFinal(decoded.copyOfRange(16, decoded.size))
        return String(decrypted)
    }

    private fun getKey(): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(SECRET_KEY.toCharArray(), SALT.toByteArray(), 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }
}
