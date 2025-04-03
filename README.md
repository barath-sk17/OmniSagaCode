# Capture The Flag (CTF) Challenge - Mobile Security

## Overview
Welcome to the Mobile Security CTF Challenge! This competition is designed to test your skills in mobile application security by identifying and exploiting various vulnerabilities. The challenges cover common security flaws found in mobile applications, including hardcoded secrets, insecure storage, XSS, and more.

## 📌 Challenges Overview
This CTF consists of ten challenges, each designed to simulate real-world security issues. Your goal is to analyze the application, identify vulnerabilities, and exploit them to retrieve the flag.

## 🔥 Challenges & Descriptions

### 1️⃣ Hardcoded Secrets
- **Description:** Sensitive data such as API keys, credentials, and access tokens are hardcoded into the application.
- **Goal:** Decompile the APK and extract the secrets.
- **Tools:** APKTool, jadx, strings, grep

### 2️⃣ Insecure Storage
- **Description:** The application improperly stores sensitive user data in shared preferences or unprotected databases.
- **Goal:** Locate and extract stored sensitive information.
- **Tools:** ADB, SQLite, Frida

### 3️⃣ Cross-Site Scripting (XSS)
- **Description:** The application fails to properly sanitize user inputs, allowing JavaScript execution.
- **Goal:** Inject malicious JavaScript code and execute it.
- **Tools:** Burp Suite, JavaScript payloads

### 4️⃣ Deep Links Exploitation
- **Description:** The application exposes deep links that can be manipulated to access restricted content or execute unintended actions.
- **Goal:** Identify and exploit deep link vulnerabilities.
- **Tools:** ADB, Drozer, Logcat

### 5️⃣ Vulnerable IPC Components
- **Description:** Insecure inter-process communication (IPC) mechanisms allow unauthorized access to app functionalities.
- **Goal:** Exploit weak IPC security to execute privileged actions.
- **Tools:** Drozer, ADB, Frida

### 6️⃣ Steganography
- **Description:** Hidden messages or flags are embedded in images or media files within the application.
- **Goal:** Extract the hidden flag using steganographic analysis.
- **Tools:** Stegsolve, zsteg, binwalk

### 7️⃣ Smali Code Manipulation
- **Description:** The application can be reverse-engineered and modified using Smali to bypass certain restrictions.
- **Goal:** Decompile, modify, and recompile the Smali code to obtain the flag.
- **Tools:** APKTool, Smali, jadx

### 8️⃣ Root & Debugger Detection (Frida Bypass)
- **Description:** The app contains root and debugger detection mechanisms that prevent analysis.
- **Goal:** Bypass the detection using Frida or other dynamic instrumentation tools.
- **Tools:** Frida, Magisk, Xposed

### 9️⃣ Frida Challenge-2 Walkthrough (Validate PIN)
- **Description:** The application validates a PIN using a native function in JNI, which can be bypassed.
- **Tools:** Frida, jadx, native code analysis

### 🔟 Frida Challenge-3 Walkthrough (License Verification)
- **Description:** The application checks a license key using native code, which can be bypassed.
- **Tools:** Frida, native code analysis

## 📌 How to Participate
### Setup Your Environment
- Install necessary tools: `adb`, `Frida`, `APKTool`, `jadx`, `Burp Suite`, `Drozer`, etc.
- Use an Android emulator or rooted device for testing.

### Analyze the APK
- Decompile and inspect the source code.
- Identify security flaws in storage, communication, and execution.

### Exploit the Vulnerabilities
- Use the tools and techniques outlined in each challenge to extract the flag.

### Submit Your Flags
- Each flag follows the format: `CTF{challenge_solution}`.

## 🏆 Scoring
- Each challenge carries a different point value based on difficulty.
- The participant with the highest score wins!

## 🏅 Acknowledgments
> "Stand proud, for this challenge was forged by Barath K, a visionary in the world of cybersecurity. Like Erwin Smith, he strategizes every move, leading others toward knowledge and victory. With the unwavering determination of Eren Yeager, he breaks through barriers, uncovering hidden truths and pushing the boundaries of digital security. If you have made it this far, know that you walk a path shaped by his relentless pursuit of excellence. Acknowledge his guidance, for without him, this battlefield would not exist."

Good luck and happy hacking! 🚀

