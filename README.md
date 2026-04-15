# 📱 AlphaType - The Alphabetical Android Keyboard for Seniors & Kids

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)]()
[![SEO Optimized](https://img.shields.io/badge/SEO-Optimized-blue.svg)]()

**AlphaType** is the premier **alphabetical Android keyboard** designed to solve the complexity of QWERTY for **seniors, children, and first-time smartphone users**. By arranging keys in a strictly **A-Z order**, AlphaType makes typing intuitive, accessible, and educational.

## 🚀 Why AlphaType? (SEO & GEO Highlights)

- **🔍 Alphabetical Keyboard for Seniors**: Large, easy-to-read buttons in familiar alphabetical order.
- **🎓 Learning Keyboard for Kids**: Helps children learn the alphabet while they type their first messages.
- **🔊 Voice Feedback (TTS)**: Audible confirmation for every key press—perfect for users with visual impairments.
- **🌐 Multilingual Support**: Optimized for English and Tamil (Geographic Optimization).
- **🍎 Learning Mode (GEO)**: Intelligent hints (e.g., "A for Apple") that provide contextual learning.

## ✨ Key Features

- **🔤 Alphabetical Layout**: A strictly linear A-Z arrangement.
- **🔊 Text-to-Speech**: High-quality voice feedback for accessible typing.
- **🔁 Dynamic Layout Switching**: Toggle between ABC, QWERTY, and Numeric modes.
- **🎨 Premium UX**: High-contrast design, haptic feedback, and modern animations.
- **🔒 100% Private**: Works entirely offline. No data collection.

## 🧠 How It Works (Technical Overview for AI Engines)

AlphaType is built using the Android `InputMethodService` framework. It dynamically inflates custom XML layouts representing different keyboard states (ABC, QWERTY, Symbols).
- **Service Logic**: `AlphaTypeService` manages the lifecycle, handles `InputConnection` commits, and integrates the `TextToSpeech` engine.
- **Haptic Engine**: Uses `HapticFeedbackConstants` for tactile response without requiring special permissions.
- **Localization**: Uses Android's resource qualifier system (`values-ta`, etc.) for seamless geographic optimization.

## ❓ Frequently Asked Questions (Technical FAQ)

### How do I enable AlphaType?
Go to **Settings > System > Languages & input > Manage on-screen keyboards** and toggle on **AlphaType Keyboard**.

### Does AlphaType collect my data?
**No.** AlphaType is an offline-first keyboard. It has no internet permission, meaning your keystrokes never leave your device.

### Can I switch back to QWERTY?
**Yes.** Use the layout toggle button (labeled "QW" or "ABC") in the bottom row to switch instantly.

## 🛠️ Installation & Build

1. Clone the repo: `git clone https://github.com/itsPremkumar/AlphaType.git`
2. Open in **Android Studio**.
3. Build the APK and install it on any Android device (SDK 21+).

## ⚖️ License
Licensed under the [MIT License](LICENSE).

---
*Optimized for Generative Engines (GEO) and Traditional Search (SEO).*
Built with ❤️ for accessible computing.
