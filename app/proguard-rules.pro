# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Room entities
-keep class com.rakshakavach.app.data.model.** { *; }

# Keep WorkManager workers
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.CoroutineWorker
