#include <jni.h>
#include <string>
#include <dlfcn.h>
#include <android/log.h>

#define LOG_TAG "CTF_NATIVE"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_ctfapp_SecurityManager_isFridaDetected(JNIEnv *env, jobject thiz) {
    const char *suspected_libs[] = {
            "frida-gadget",
            "frida-agent",
            "libfrida",
            "gum-js-loop"
    };

    // Loop through the suspected FRIDA libraries and check if they're loaded
    for (const char *lib_name : suspected_libs) {
        void *handle = dlopen(lib_name, RTLD_NOW);
        if (handle != NULL) {
            LOGE("FRIDA detected: %s", lib_name); // Log which library was detected
            dlclose(handle); // Close the library handle
            return JNI_TRUE; // FRIDA detected
        }
    }

    LOGI("No FRIDA detected."); // Log that no FRIDA library was found
    return JNI_FALSE; // FRIDA not detected
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_ctfapp_SecurityManager_validatePIN(JNIEnv *env, jobject thiz, jstring pin) {
    const char *inputPin = env->GetStringUTFChars(pin, nullptr); // Get the PIN as a C string
    const char *correctPin = "7357"; // Correct PIN for validation

    if (inputPin == nullptr) {
        LOGE("Failed to convert PIN to UTF string.");
        return JNI_FALSE;
    }

    // Validate the PIN
    jboolean isValid = JNI_FALSE;
    if (strcmp(inputPin, correctPin) == 0) {
        LOGI("PIN validated successfully!");
        isValid = JNI_TRUE;
    } else {
        LOGE("Invalid PIN entered: %s", inputPin); // Log the invalid PIN (may want to avoid logging PIN in production)
    }

    // Always release the UTF string to prevent memory leaks
    env->ReleaseStringUTFChars(pin, inputPin);

    return isValid;
}
