# Git Commands — Fix Your RakshaKavach Repo
# Run these one by one in your terminal (inside the project folder)

# ─────────────────────────────────────────────
# STEP 1: Add the .gitignore FIRST
# (this stops IDE/build files from being tracked)
# ─────────────────────────────────────────────

# Copy the .gitignore file into your project root, then:
git rm -r --cached .gradle
git rm -r --cached .idea
git rm --cached local.properties
git add .gitignore
git commit -m "chore: add .gitignore, remove IDE and build files"


# ─────────────────────────────────────────────
# STEP 2: Add README.md
# ─────────────────────────────────────────────

# Copy README.md into your project root, then:
git add README.md
git commit -m "docs: add README with setup instructions and features"


# ─────────────────────────────────────────────
# STEP 3: Add meaningful commits per feature
# (run each separately — don't batch them)
# ─────────────────────────────────────────────

git add app/src/main/java/com/rakshakavach/app/ui/auth/
git commit -m "feat: implement login and registration with session management"

git add app/src/main/java/com/rakshakavach/app/ui/home/
git commit -m "feat: add home dashboard with safety score and feature cards"

git add app/src/main/java/com/rakshakavach/app/ui/checklist/
git commit -m "feat: add task selector and gear checklist with progress tracking"

git add app/src/main/java/com/rakshakavach/app/ui/riskmeter/
git commit -m "feat: add risk meter with real-time gear compliance gauge"

git add app/src/main/java/com/rakshakavach/app/ui/incident/
git commit -m "feat: add incident log with Room DB persistence"

git add app/src/main/java/com/rakshakavach/app/ui/quiz/
git commit -m "feat: add daily safety quiz with timer and scoring"

git add app/src/main/java/com/rakshakavach/app/ui/score/
git commit -m "feat: add safety score screen with gamification levels"

git add app/src/main/java/com/rakshakavach/app/ui/profile/
git commit -m "feat: add editable profile with work details"

git add app/src/main/java/com/rakshakavach/app/notification/
git commit -m "feat: add WorkManager daily notification with BootReceiver"

git add app/src/main/java/com/rakshakavach/app/data/
git commit -m "feat: set up Room DB with IncidentLog entity and DAO"

git add app/src/main/java/com/rakshakavach/app/viewmodel/
git commit -m "feat: add MainViewModel with LiveData for score and streak"


# ─────────────────────────────────────────────
# STEP 4: Push everything to GitHub
# ─────────────────────────────────────────────

git push origin master


# ─────────────────────────────────────────────
# STEP 5: Add repo description on GitHub
# ─────────────────────────────────────────────
# Go to https://github.com/Lav-AL/RakshaKavach
# Click the ⚙️ gear icon next to "About"
# Add description: "Android worker safety auditor — PPE checklists, incident logging, daily quiz & gamification. MindMatrix VTU Internship #42"
# Add topics: android, safety, kotlin, mvvm, room-database, workmanager, internship
