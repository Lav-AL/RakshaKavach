[README.md](https://github.com/user-attachments/files/27790029/README.md)
# 🦺 Raksha-Kavach — Worker Safety Auditor

> **Raksha-Kavach** (Sanskrit: *Shield & Armour*) — An Android app that digitizes daily safety compliance for construction and manufacturing workers.

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Language](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24%20(Android%207.0)-blue.svg)](https://developer.android.com)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34%20(Android%2014)-blue.svg)](https://developer.android.com)
[![Internship](https://img.shields.io/badge/MindMatrix-VTU%20Internship%20%2342-yellow.svg)]()

---

## 📌 Problem Statement

Industrial accidents in India cause over **48,000 deaths annually** *(Labour Bureau, GoI)*. Most are preventable through proper PPE usage and hazard awareness. Raksha-Kavach replaces paper checklists, verbal reminders, and manual logs with a structured, gamified mobile workflow — all fully offline.

---

## ✨ Features

| Module | Description |
|--------|-------------|
| 🔐 **Authentication** | Local registration & login with session management via SharedPreferences |
| 🏠 **Home Dashboard** | Safety score, streak, gear progress, and quick-access feature cards |
| 👤 **Profile** | Editable personal & work details with safety stats |
| ✅ **Gear Checklist** | Task-based PPE verification across 10 construction tasks |
| ⚠️ **Risk Meter** | Real-time risk gauge (EXTREME → SAFE) based on gear compliance |
| 📋 **Incident Log** | Near-miss reporting with severity classification, stored in Room DB |
| 🧠 **Daily Safety Quiz** | 5 timed MCQs from a question bank with instant feedback |
| 🏆 **Safety Score** | Points-based gamification with streaks and safety level progression |
| 🔔 **Notifications** | Daily 7 AM WorkManager reminder, survives device reboot via BootReceiver |

---

## 🎮 Gamification

| Action | Points |
|--------|--------|
| Mark Safe Day (≥80% gear) | +10 |
| Complete checklist 100% | +10/day |
| Perfect quiz (5/5) | +30 |
| 7-day streak bonus | +50 |
| Report Near Miss | -20 |

**Safety Levels:** Beginner → Learning → Safety Aware → Safety Pro → Safety Master → Safety Champion 🏆

---

## 🏗️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java (Android) |
| Architecture | MVVM (Model-View-ViewModel) |
| Database | Room ORM (SQLite) |
| State Management | LiveData + ViewModel |
| Background Work | WorkManager |
| UI | Material Components 3, XML Layouts |
| Auth | Local SharedPreferences |

---

## 📁 Project Structure

```
com.rakshakavach.app/
├── data/
│   ├── database/       IncidentDao, RakshaKavachDatabase
│   └── model/          IncidentLog, Task, QuizQuestion
├── notification/       SafetyReminderWorker, BootReceiver
├── viewmodel/          MainViewModel
└── ui/
    ├── auth/           LoginActivity, RegisterActivity
    ├── home/           MainActivity
    ├── profile/        ProfileActivity
    ├── checklist/      ChecklistActivity, GearChecklistActivity
    ├── riskmeter/      RiskMeterActivity
    ├── incident/       IncidentLogActivity
    ├── quiz/           SafetyQuizActivity
    ├── score/          SafetyScoreActivity
    └── splash/         SplashActivity
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio **Hedgehog (2023.1.1)** or newer
- JDK **17**
- Android SDK **34** (Android 14)
- Android device or emulator running **Android 7.0 (API 24)** or higher

### Build & Run

```bash
# 1. Clone the repository
git clone https://github.com/Lav-AL/RakshaKavach.git

# 2. Open in Android Studio
#    File → Open → select the RakshaKavach folder

# 3. Let Gradle sync automatically

# 4. Run the app
#    Click Run ▶ or press Shift+F10
#    Select your emulator or connected device
```

### First Launch

1. Register with your name, email, phone, and password
2. Select a task from the Task Selector
3. Verify your PPE gear on the checklist
4. Mark a Safe Day to start earning points!

---

## 📦 Dependencies

```gradle
// AndroidX
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

// Material Design 3
implementation 'com.google.android.material:material:1.11.0'

// Room Database
implementation 'androidx.room:room-runtime:2.6.1'
ksp 'androidx.room:room-compiler:2.6.1'

// WorkManager
implementation 'androidx.work:work-runtime-ktx:2.9.0'

// ViewModel + LiveData
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
```

---

## 🎯 Target Users

- **Ramesh** — Site worker who forgets PPE on busy days
- **Priya** — Safety supervisor who needs to track compliance
- **Arjun** — New joiner who needs to learn which PPE is required

---

## 📊 Success Metrics

| Metric | Target |
|--------|--------|
| Daily Active Users | ≥ 70% of registered workers |
| Gear Checklist Completion | ≥ 85% per working day |
| Quiz Participation | ≥ 60% of active users |
| App Crash Rate | < 0.5% of sessions |

---

## 🗺️ Roadmap (v2.0+)

- [ ] Firebase cloud sync & supervisor dashboard
- [ ] Hindi / Kannada / Tamil language support
- [ ] Camera-based AI gear detection (ML Kit)
- [ ] QR code site check-in with geofencing
- [ ] Wear OS companion app

---

## 👨‍💻 Team

**MindMatrix VTU Internship — Project #42**

| Name | Role |
|------|------|
| Lav AL | Android Developer |

---

## 📄 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

*Built with ❤️ for worker safety in India*
