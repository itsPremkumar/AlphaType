# 📱 AlphaType - The Professional & Private Android Keyboard

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![Privacy](https://img.shields.io/badge/Privacy-100%25%20Offline-blue.svg)]()

**AlphaType** is a premium, **privacy-first Android keyboard** designed for users who demand a secure, professional, and intuitive typing experience. Featuring a sophisticated **Midnight Graphite** design and fully customizable feedback, AlphaType combines modern aesthetics with a hardened security posture.

## 🚀 Why AlphaType? (Professional Highlights)

- **🛡️ 100% Private & Secure**: Zero data collection. Offline-only architecture ensures your keystrokes never leave your device.
- **🌑 Midnight Graphite Aesthetics**: A sleek, dark-themed UI with indigo accents and optimized 8dp key layouts for a premium feel.
- **⚙️ Precise Configuration**: Dedicated in-app settings to toggle **Voice Feedback** and **Tactile Haptics** ON or OFF.
- **🔊 Optional Voice Feedback**: Integrated Text-to-Speech support for high-accessibility typing, fully controllable via the setup menu.
- **🌐 Multilingual Capable**: Professional layouts for English and Tamil, optimized for geographic accuracy.

## ✨ Key Features

- **🎨 Premium Dark Theme**: Immersive, translucent UI that adapts to modern Android design standards.
- **🔁 Versatile Layouts**: Instant toggling between **Alphabetical (ABC)**, **Standard (QWERTY)**, and **Numeric** modes.
- **🎚️ Customizable Feedback**: Independent controls for Audible (TTS) and Tactile (Vibration) feedback.
- **⌨️ Optimized Typograpy**: Professional key labeling and spacing for reduced errors and faster input.
- **🔒 Hardened Security**: No internet permission, no analytics, no persistent tracking.

## ⚙️ Setup & Configuration

AlphaType features a dedicated **Setup Activity** that guides you through the process:
1. **Activate Privacy**: Enable the keyboard service in your system settings.
2. **Standard Set**: Establish AlphaType as your secure primary input method.
3. **Personalize**: Use the **General Settings** card to calibrate Voice and Haptic feedback to your professional needs.

## 🧠 Technical Overview (For Developers & AI)

AlphaType is built on a clean, maintainable Kotlin codebase using the Android `InputMethodService` framework.
- **Persistence**: Uses a custom `SettingsManager` for lightweight, non-tracking preference storage.
- **Dynamic Inflation**: Layouts are managed via a reactive container system for seamless transitions.
- **Haptic Engine**: Leverages system `HapticFeedbackConstants` for zero-latency tactile response.

## 🛠️ Installation & Build

1. Clone the repo: `git clone https://github.com/itsPremkumar/AlphaType.git`
2. Open in **Android Studio**.
3. Build the APK via `gradlew assembleDebug`.
4. Deploy to any Android device (SDK 21+).

## ⚖️ License
Licensed under the [MIT License](LICENSE).

---
*AlphaType: Private, Secure, Professional. Built with ❤️ for the privacy-conscious user.*
