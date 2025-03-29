#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ctfapp_SecretManager_getEncodedSecret(JNIEnv *env, jobject) {
    std::string encodedSecret = "52 42 41 41 65 42 33 5a 47 70 31 45 46 31 51 4a 74 4a 30 53 5a 52 6c 4f 5a 46 77 42 42 55 30 5a 59 39 55 4d 3d 6b 78 46";
    return env->NewStringUTF(encodedSecret.c_str());
}

/*
 import base64

def hex_to_bytes(hex_string):
    """Convert hex string to byte array"""
    hex_values = hex_string.split()
    return bytes(int(h, 16) for h in hex_values)

def reverse_hex_chunks(hex_string, chunk_size=4):
    """Reverse every chunk of hex values"""
    hex_values = hex_string.split()
    reversed_chunks = []

    for i in range(0, len(hex_values), chunk_size):
        reversed_chunks.extend(hex_values[i:i+chunk_size][::-1])

    return " ".join(reversed_chunks)

def xor_decrypt(data, key):
    """Perform XOR decryption"""
    return ''.join(chr(byte ^ ord(key[i % len(key)])) for i, byte in enumerate(data))

# Given input (retrieved from getme)
hex_input = "52 42 41 41 65 42 33 5a 47 70 31 45 46 31 51 4a 74 4a 30 53 5a 52 6c 4f 5a 46 77 42 42 55 30 5a 59 39 55 4d 3d 6b 78 46"

# Reverse the hex chunks
reversed_hex = reverse_hex_chunks(hex_input)

# Convert to bytes
decoded_bytes = hex_to_bytes(reversed_hex)

# Decode as UTF-8 string
decoded_string = decoded_bytes.decode("utf-8")
print(f"Decoded Base64 String: {decoded_string}")

# Decode Base64
base64_decoded = base64.b64decode(decoded_string)

# XOR key (same as in the Kotlin code)
xor_key = "cd7862r=="

# Perform XOR decryption
flag = xor_decrypt(base64_decoded, xor_key)

print(f"FLAG: {flag}")

 * */