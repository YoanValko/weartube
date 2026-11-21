# 🎬 WearTube

<div align="center">

![Android](https://img.shields.io/badge/Android-3ED0F0?style=for-the-badge&logo=android&logoColor=white)
![Wear OS](https://img.shields.io/badge/Wear%20OS-000000?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![API 36](https://img.shields.io/badge/API%2036-000000?style=for-the-badge&logo=android&logoColor=white)

<h3>A Wear OS YouTube Client for Pixel Watch</h3>
<p>Watch YouTube videos directly on your wrist with Jetpack Compose for Wear OS</p>

[![GitHub stars](https://img.shields.io/github/stars/YoanValko/weartube?style=social)](https://github.com/YoanValko/weartube/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/YoanValko/weartube?style=social)](https://github.com/YoanValko/weartube/network/members)
[![GitHub issues](https://img.shields.io/github/issues/YoanValko/weartube)](https://github.com/YoanValko/weartube/issues)
[![GitHub license](https://img.shields.io/github/license/YoanValko/weartube)](LICENSE)
[![GitHub release](https://img.shields.io/github/v/release/YoanValko/weartube?include_prereleases)](https://github.com/YoanValko/weartube/releases)

</div>

---

<div align="center">

## 📱 Screenshots

<table>
  <tr>
    <td><b>🏠 Home Screen</b></td>
    <td><b>▶️ Video Player</b></td>
  </tr>
  <tr>
    <td>
      <img src="https://raw.githubusercontent.com/YoanValko/weartube/main/screenshots/home.png" width="220"/>
    </td>
    <td>
      <img src="https://raw.githubusercontent.com/YoanValko/weartube/main/screenshots/player.png" width="220"/>
    </td>
  </tr>
</table>

<p><em>Tap any video to open it in the built-in YouTube player</em></p>

</div>

---

## ✨ Features

<table>
  <tr>
    <td>🎥</td>
    <td><b>YouTube Video Playback</b> — Watch videos directly on your Pixel Watch via stream extraction</td>
  </tr>
  <tr>
    <td>🎨</td>
    <td><b>Jetpack Compose for Wear OS</b> — Modern, declarative UI built with Material3 design</td>
  </tr>
  <tr>
    <td>⚡</td>
    <td><b>Fast & Lightweight</b> — Minimal dependencies, optimized for watch hardware</td>
  </tr>
  <tr>
    <td>🔄</td>
    <td><b>Bottom Navigation</b> — Quick access to Home, Subscriptions, Search, and Library</td>
  </tr>
  <tr>
    <td>🔙</td>
    <td><b>Back Button Handling</b> — Proper back navigation that closes the player instead of exiting</td>
  </tr>
  <tr>
    <td>⚙️</td>
    <td><b>Wear OS 6.0</b> — Built for API 36 with watch-specific UI patterns</td>
  </tr>
</table>

---

## 🏗️ How It Works

WearTube uses YouTube's **Innertube API** via the [TubeRepair](https://github.com/kevinf100/tuberepair.uptimetrackers.com) proxy to extract direct video stream URLs — no WebView required (which is disabled by default on Wear OS signed system images).

### Tech Stack

```text
┌─────────────────────────────────────────────────────────────────┐
│                         WearTube UI                             │
│                    Jetpack Compose + Material3                  │
├─────────────────────────────────────────────────────────────────┤
│                    YouTubeStreamExtractor                       │
│              TubeRepair API (HTTP GET → 307 redirect)           │
├─────────────────────────────────────────────────────────────────┤
│                     Android VideoView                           │
│                    Direct MP4 Stream Playback                   │
└─────────────────────────────────────────────────────────────────┘
```

### Flow

1. User taps a video → Video ID captured
2. `YouTubeStreamExtractor` calls `https://tuberepair.uptimetrackers.com/360/getvideo/<video_id>`
3. TubeRepair follows redirects to find the direct MP4 stream URL
4. `VideoView` plays the stream directly on the watch

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest)
- Android SDK with API 36 (Wear OS 6.0)
- Wear OS 6.0 emulator or physical Pixel Watch
- Java 17+

### Quick Start

```bash
# Clone the repository
git clone https://github.com/YoanValko/weartube.git
cd weartube

# Build the debug APK
./gradlew assembleDebug

# Install on Wear OS emulator
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch the app
adb shell am start -n com.ytclient.watch/com.ytclient.watch.MainActivity
```

### Run on Emulator

```bash
# Start the Wear OS 6.0 emulator
emulator @WearOS6 -no-snapshot-load -no-audio -gpu swiftshader_indirect -port 5554

# Wait for boot (~60 seconds), then install
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.ytclient.watch/com.ytclient.watch.MainActivity
```

---

## 📂 Project Structure

```
weartube/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/ytclient/watch/
│   │       │   ├── MainActivity.kt      # Main app entry & Compose UI
│   │       │   └── YouTubeStreamExtractor.kt  # Stream URL extraction
│   │       ├── res/
│   │       │   ├── values/              # Themes, colors, strings
│   │       │   ├── drawable/            # Icons & graphics
│   │       │   └── mipmap/              # App launcher icons
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── gradle.properties
└── README.md
```

---

## 📋 Usage

1. **Launch the app** from your Pixel Watch app drawer
2. **Browse videos** on the Home screen (curated video list)
3. **Tap a video** to open the video player
4. **Watch the video** using YouTube stream URLs fetched via TubeRepair API
5. **Press the back button** to close the player and return to Home
6. **Navigate tabs** at the bottom: Home, Subscriptions, Search, Library

---

## 🛣️ Roadmap

| Status | Feature |
|--------|---------|
| ✅ | Basic Jetpack Compose UI for Wear OS |
| ✅ | Video list with thumbnails and metadata |
| ✅ | YouTube stream URL extraction via TubeRepair API |
| ✅ | VideoView playback integration |
| ✅ | Back button handling (close player, not exit app) |
| ⬜ | YouTube Data API v3 integration for real video listings |
| ⬜ | Search functionality |
| ⬜ | Video subscriptions |
| ⬜ | Offline video caching |
| ⬜ | Multiple resolution support (144p, 360p, 720p) |

---

## 🤝 Contributing

Contributions are welcome! Here's how to get started:

1. **Fork** the repository
2. **Create** your feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add some amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## ⚠️ Disclaimer

WearTube is a third-party YouTube client. It uses the TubeRepair API to extract video stream URLs. This project is **not affiliated with Google, YouTube, or Alphabet Inc.** Use at your own risk and respect YouTube's Terms of Service.

---

<div align="center">

## 🙌 Support

If you like this project, please give it a ⭐ on GitHub!

</div>

<div align="center">

[🔗 **GitHub Repository**](https://github.com/YoanValko/weartube) • 
[🐛 **Report Issues**](https://github.com/YoanValko/weartube/issues) • 
[📦 **Releases**](https://github.com/YoanValko/weartube/releases)

</div>

<div align="center">

**Made with ❤️ for Wear OS**

</div>
