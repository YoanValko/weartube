# 🎬 WearTube

<div align="center">

[![Android](https://img.shields.io/badge/Android-3ED0F0?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Wear OS](https://img.shields.io/badge/Wear%20OS-000000?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/wear/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)

**A Wear OS YouTube client for Pixel Watch**

Built with Jetpack Compose for Wear OS • YouTube video playback via TubeRepair API

</div>

---

## 📱 Screenshots

<div align="center">

<table>
  <tr>
    <td><b>Home Screen</b></td>
    <td><b>Video Player</b></td>
  </tr>
  <tr>
    <td>
      <img src="https://raw.githubusercontent.com/YoanValko/weartube/main/screenshots/home.png" width="200"/>
    </td>
    <td>
      <img src="https://raw.githubusercontent.com/YoanValko/weartube/main/screenshots/player.png" width="200"/>
    </td>
  </tr>
</table>

</div>

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🎥 **YouTube Video Playback** | Watch videos directly on your Pixel Watch via TubeRepair API stream extraction |
| 🎨 **Jetpack Compose for Wear OS** | Modern, declarative UI built with Material3 design |
| ⚡ **Fast & Lightweight** | Minimal dependencies, optimized for watch hardware |
| 🔄 **Bottom Navigation** | Quick access to Home, Subscriptions, Search, and Library |
| 🔙 **Back Button Handling** | Proper back navigation that closes the player instead of exiting the app |
| ⚙️ **Wear OS 6.0** | Built for API 36 with watch-specific UI patterns |

---

## 🏗️ How It Works

WearTube uses YouTube's **Innertube API** via the [TubeRepair](https://github.com/kevinf100/tuberepair.uptimetrackers.com) proxy to extract direct video stream URLs. This approach:

1. Sends a request to `https://tuberepair.uptimetrackers.com/360/getvideo/<video_id>`
2. Receives a direct MP4 stream URL (307 redirect resolved)
3. Plays the stream using Android's built-in `VideoView`

This avoids WebView entirely, which is disabled by default on Wear OS signed system images.

### Tech Stack

```
┌─────────────────────────────────────────────────────────┐
│                    WearTube UI                          │
│              Jetpack Compose + Material3                │
├─────────────────────────────────────────────────────────┤
│                    YouTubeStreamExtractor               │
│              TubeRepair API (HTTP GET)                  │
├─────────────────────────────────────────────────────────┤
│                    Android VideoView                    │
│                  Direct MP4 Stream Playback             │
└─────────────────────────────────────────────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest)
- Android SDK with API 36
- Wear OS 6.0 emulator or physical Pixel Watch
- Java 17+

### Build & Run

```bash
# Clone the repository
git clone https://github.com/YoanValko/weartube.git
cd weartube

# Build the APK
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

# Wait for boot, then install
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.ytclient.watch/com.ytclient.watch.MainActivity
```

---

## 📂 Project Structure

```
D:\YTClientWatch\
├── app\
│   ├── src\
│   │   ├── main\
│   │   │   ├── java\com\ytclient\watch\
│   │   │   │   ├── MainActivity.kt      # Main app entry & Compose UI
│   │   │   │   └── YouTubeStreamExtractor.kt  # Stream URL extraction
│   │   │   ├── res\
│   │   │   │   ├── values\              # Themes, colors, strings
│   │   │   │   ├── drawable\            # Icons & graphics
│   │   │   │   └── mipmap\              # App launcher icons
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
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
5. **Press the back button** to close the player and return to Home (not exit the app!)
6. **Navigate tabs** at the bottom: Home, Subscriptions, Search, Library

---

## 🛣️ Roadmap

- [x] Basic Jetpack Compose UI for Wear OS
- [x] Video list with thumbnails and metadata
- [x] YouTube stream URL extraction via TubeRepair API
- [x] VideoView playback integration
- [x] Back button handling (close player, not exit app)
- [ ] YouTube Data API v3 integration for real video listings
- [ ] Search functionality
- [ ] Video subscriptions
- [ ] Offline video caching
- [ ] Multiple resolution support (144p, 360p, 720p)

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## ⚠️ Disclaimer

WearTube is a third-party YouTube client. It uses the TubeRepair API to extract video stream URLs. This project is not affiliated with Google, YouTube, or Alphabet Inc. Use at your own risk and respect YouTube's Terms of Service.

---

<div align="center">

**Made with ❤️ for Wear OS**

[GitHub Repository](https://github.com/YoanValko/weartube) • [Issues](https://github.com/YoanValko/weartube/issues) • [Releases](https://github.com/YoanValko/weartube/releases)

</div>
