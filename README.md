# 🛡️ Raksha-Kavach — Worker Safety Auditor

> *Raksha-Kavach (Sanskrit: Shield & Armour)* — A comprehensive Android application for industrial workplace safety compliance, designed for construction and manufacturing workers.

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android)](https://android.com)
[![Language](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24%20(Android%207.0)-orange)](https://developer.android.com)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue)](https://developer.android.com/topic/architecture)
[![Build](https://img.shields.io/badge/Build-Gradle%208.9-blue)](https://gradle.org)

---

## 📱 Screenshots

> App uses an **industrial Yellow/Black** theme for high visibility in construction environments.

| Splash | Login | Register |
|--------|-------|----------|
| Safety-branded launch screen | Email/Password auth | Full worker registration |

| Home Dashboard | Task Selector | Gear Checklist |
|----------------|---------------|----------------|
| Score, streak, all modules | 10 task types with risk levels | Per-task PPE verification |

| Risk Meter | Incident Log | Safety Quiz | Profile |
|------------|--------------|-------------|---------|
| Live risk gauge | Near-miss reporting | Daily 5-question quiz | Editable worker profile |

---

## 🚀 Features

### 🔐 Authentication
- Worker **Registration** (name, email, phone, job role, password)
- **Login** with local credential validation (SharedPreferences)
- Session persistence — stays logged in across app restarts
- Animated form validation with shake/error feedback

### 🏠 Home Dashboard
- Personal avatar with auto-generated initials
- Real-time **Safety Score** and **Safe Day Streak**
- Daily task banner with gear completion progress
- Quick-launch cards for all 6 modules
- **Mark Safe Day** button (requires ≥80% gear compliance)

### 👤 Worker Profile
- Editable personal info: name, phone, blood group, bio
- Emergency contact field (critical safety data)
- Work details: company, site, job role, years of experience, certifications
- Safety stats: score, streak, incidents logged, quizzes taken

### 📋 Task Selector & Gear Checklist
- 10 pre-defined industrial tasks (Welding, Height Work, Electrical, Chemical Handling, etc.)
- Each task has a **risk level** (EXTREME / HIGH / MEDIUM / LOW)
- Gear checklist opens as a **separate screen** with task-specific PPE list
- Large checkboxes (glove-friendly UI)
- Live progress bar + dynamic status message (❌ → ⚠️ → ✅)

### ⚠️ Risk Meter
- Calculates real-time risk based on gear compliance percentage
- Visual risk gauge with color-coded levels
- Worker avatar showing equipped/missing gear
- Task-specific injury warnings for missing PPE

### 📝 Incident Log (Near-Miss Reporting)
- Report types: Near Miss, Equipment Failure, PPE Violation, Unsafe Condition, Slip/Fall
- Severity classification (LOW / MEDIUM / HIGH)
- Persistent log stored in **Room database**
- Swipe-to-delete incident records
- Auto-deducts -20 safety points per report

### 🎯 Daily Safety Quiz
- 5 randomized questions from a 10-question safety knowledge bank
- 30-second countdown timer per question
- Immediate answer feedback with explanation
- Scoring: +30 (5/5) / +20 (4/5) / +10 (3/5)

### 🏆 Safety Score & Gamification
- Points system with 6 safety levels (Beginner → Safety Champion)
- Safe day streak tracker
- Score persists across sessions (SharedPreferences)

### 🔔 Daily Notifications
- 7:00 AM daily safety reminder via **WorkManager**
- Survives device reboot (`BootReceiver`)

---

## 🏗️ Architecture & Tech Stack

```
┌─────────────────────────────────┐
│           UI Layer              │
│  Activities · Layouts · Anims   │
├─────────────────────────────────┤
│         ViewModel Layer         │
│      MainViewModel (MVVM)       │
├─────────────────────────────────┤
│          Data Layer             │
│  Room DB · SharedPreferences    │
│  TaskRepository · QuizBank      │
├─────────────────────────────────┤
│       System Services           │
│  WorkManager · BootReceiver     │
└─────────────────────────────────┘
```

| Component | Technology |
|-----------|------------|
| **Language** | Kotlin |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Database** | Room (SQLite) |
| **State Management** | LiveData + ViewModel |
| **Background Tasks** | WorkManager |
| **UI** | Material Components 3, ViewBinding |
| **Auth & Prefs** | SharedPreferences |
| **Build System** | Gradle 8.9 + AGP 8.5.0 |
| **Min SDK** | API 24 (Android 7.0) |
| **Target SDK** | API 34 (Android 14) |

---

## 📁 Project Structure

```
RakshaKavach/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/rakshakavach/app/
│       │   ├── data/
│       │   │   ├── database/
│       │   │   │   ├── IncidentDao.kt          # Room DAO
│       │   │   │   └── RakshaKavachDatabase.kt # Room DB singleton
│       │   │   └── model/
│       │   │       ├── IncidentLog.kt          # Room entity
│       │   │       ├── Task.kt                 # Task data class
│       │   │       ├── TaskRepository.kt       # 10 predefined tasks
│       │   │       └── QuizQuestion.kt         # Quiz data + bank
│       │   ├── notification/
│       │   │   ├── SafetyReminderWorker.kt     # WorkManager worker
│       │   │   └── BootReceiver.kt             # Reboot persistence
│       │   ├── viewmodel/
│       │   │   └── MainViewModel.kt            # Shared ViewModel
│       │   └── ui/
│       │       ├── auth/
│       │       │   ├── LoginActivity.kt
│       │       │   └── RegisterActivity.kt
│       │       ├── home/MainActivity.kt
│       │       ├── profile/ProfileActivity.kt
│       │       ├── checklist/
│       │       │   ├── ChecklistActivity.kt    # Task selector
│       │       │   └── GearChecklistActivity.kt
│       │       ├── riskmeter/RiskMeterActivity.kt
│       │       ├── incident/IncidentLogActivity.kt
│       │       ├── quiz/SafetyQuizActivity.kt
│       │       ├── score/SafetyScoreActivity.kt
│       │       └── splash/SplashActivity.kt
│       └── res/
│           ├── anim/        # 8 transition animations
│           ├── drawable/    # Vector icons, shapes
│           ├── layout/      # 11 activity + 3 item layouts
│           ├── mipmap/      # App icons
│           └── values/      # colors, strings, themes
├── gradle/wrapper/
│   └── gradle-wrapper.properties  # Gradle 8.9
├── build.gradle                    # AGP 8.5.0
├── gradle.properties               # AndroidX, JVM flags
└── .gitignore
```

---

## ⚙️ Build & Run

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17** or JDK 21
- Android SDK with API 24–34

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/Lav-AL/RakshaKavach.git

# 2. Open in Android Studio
#    File → Open → Select the RakshaKavach folder

# 3. Let Gradle sync complete automatically

# 4. Run on device/emulator
#    Run → Run 'app'  (or Shift+F10)
```

> **No internet connection required.** The app runs entirely offline.

### Build from command line (optional)
```bash
cd RakshaKavach
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

---

## 🗄️ Database Schema

### `IncidentLog` (Room Entity)
| Column | Type | Description |
|--------|------|-------------|
| `id` | Int (PK, auto) | Auto-generated ID |
| `taskName` | String | Selected task at time of incident |
| `incidentType` | String | Type classification |
| `description` | String | Worker's description |
| `location` | String | Site location |
| `severity` | String | LOW / MEDIUM / HIGH |
| `timestamp` | Long | Unix timestamp (ms) |

---

## 🎮 Gamification & Points

| Action | Points |
|--------|--------|
| Mark Safe Day (≥80% gear) | +10 |
| Perfect Quiz (5/5) | +30 |
| Good Quiz (4/5) | +20 |
| Average Quiz (3/5) | +10 |
| Log Near-Miss Incident | -20 |

| Level | Score Range |
|-------|-------------|
| 🔰 Beginner | 0–99 |
| 👍 Safety Aware | 100–199 |
| ⭐ Safety Pro | 200–399 |
| 🏆 Safety Champion | 400+ |

---

## 🔮 Roadmap (v2.0)

- [ ] Firebase cloud sync & supervisor dashboard
- [ ] Hindi / Kannada regional language support
- [ ] Camera-based AI gear detection (ML Kit)
- [ ] QR code site check-in / geofencing
- [ ] PDF safety audit report export
- [ ] Wear OS companion app

---

## 👨‍💻 Developer

**VK445** — MindMatrix VTU Internship  
Project #42 — Industrial Safety Auditor Application

---

## 📄 License

This project is developed as part of the **MindMatrix Internship Program** for educational purposes.

---

*⚠️ Safety is not an option — it's a DUTY*
