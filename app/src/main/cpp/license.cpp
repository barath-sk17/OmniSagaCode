#include <jni.h>
#include <string>
#include <ctime>

std::string encryptLicense(const std::string& license) {
    std::string encrypted = license;
    int key = static_cast<int>(std::time(0)) % 256;  // Use current time as key

    for (size_t i = 0; i < encrypted.length(); i++) {
        encrypted[i] ^= key;  // XOR with key
    }

    return encrypted;
}

std::string decryptLicense(const std::string& encryptedLicense) {
    std::string decrypted = encryptedLicense;
    int key = static_cast<int>(std::time(0)) % 256;  // Use current time as key

    for (size_t i = 0; i < decrypted.length(); i++) {
        decrypted[i] ^= key;  // XOR with key
    }

    return decrypted;
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_example_ctfapp_LicenseActivity_checkLicense(JNIEnv *env, jobject thiz) {
    // Encrypted license key (simulated encrypted key)
    std::string encryptedKey = "1C2C1F3F141F040604010B141B070F";  // Example encrypted license key

    std::string decryptedKey = decryptLicense(encryptedKey);

    std::string correctKey = "VALID-123-SECRET";  // Correct license key

    // Compare decrypted key with the valid key
    if (decryptedKey == correctKey) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}
