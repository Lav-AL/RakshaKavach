package com.rakshakavach.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.rakshakavach.app.R
import com.rakshakavach.app.databinding.ActivitySplashBinding
import com.rakshakavach.app.notification.SafetyReminderWorker
import com.rakshakavach.app.ui.auth.LoginActivity
import com.rakshakavach.app.ui.home.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Schedule daily safety reminder
        SafetyReminderWorker.scheduleDailyReminder(this)

        // Animate logo
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.ivShield.startAnimation(fadeIn)
        binding.tvAppName.startAnimation(slideUp)
        binding.tvTagline.startAnimation(slideUp)

        // Navigate after 2.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            routeUser()
        }, 2500)
    }

    private fun routeUser() {
        val prefs = getSharedPreferences("raksha_auth", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        val destination = if (isLoggedIn) {
            // Send start-of-day notification for returning users
            SafetyReminderWorker.sendImmediateReminder(this)
            MainActivity::class.java
        } else {
            LoginActivity::class.java
        }

        startActivity(Intent(this, destination))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
