package com.rakshakavach.app.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rakshakavach.app.R
import com.rakshakavach.app.databinding.ActivityMainBinding
import com.rakshakavach.app.ui.auth.LoginActivity
import com.rakshakavach.app.ui.checklist.ChecklistActivity
import com.rakshakavach.app.ui.incident.IncidentLogActivity
import com.rakshakavach.app.ui.profile.ProfileActivity
import com.rakshakavach.app.ui.quiz.SafetyQuizActivity
import com.rakshakavach.app.ui.riskmeter.RiskMeterActivity
import com.rakshakavach.app.ui.score.SafetyScoreActivity
import com.rakshakavach.app.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* handle permission result */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()
        setupObservers()
        setupClickListeners()
        loadUserGreeting()
    }

    private fun loadUserGreeting() {
        val prefs = getSharedPreferences("raksha_auth", MODE_PRIVATE)
        val name = prefs.getString("user_name", "Worker") ?: "Worker"
        val role = prefs.getString("user_role", "Construction Worker") ?: "Construction Worker"

        binding.tvWorkerName.text = name
        binding.tvWorkerRole.text = role

        // Avatar initials
        val initials = name.split(" ")
            .filter { it.isNotEmpty() }.take(2)
            .joinToString("") { it.first().uppercase() }
        binding.tvAvatarInitials.text = initials.ifEmpty { "?" }

        // Tap avatar or name → open profile
        val openProfile = View.OnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        binding.tvAvatarInitials.setOnClickListener(openProfile)
        binding.tvWorkerName.setOnClickListener(openProfile)
        binding.tvWorkerRole.setOnClickListener(openProfile)

        binding.btnLogout.setOnClickListener { confirmLogout() }
    }

    private fun confirmLogout() {
        MaterialAlertDialogBuilder(this)
            .setTitle("🚪 Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ -> performLogout() }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performLogout() {
        getSharedPreferences("raksha_auth", MODE_PRIVATE)
            .edit().putBoolean("is_logged_in", false).apply()
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()
        updateTaskDisplay()
        loadUserGreeting()   // refresh name/avatar if profile was edited
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupObservers() {
        viewModel.safetyScore.observe(this) { score ->
            binding.tvScoreValue.text = score.toString()
            binding.progressScore.progress = score.coerceAtMost(500)
            updateScoreLevel(score)
        }

        viewModel.streak.observe(this) { streak ->
            binding.tvStreakValue.text = "$streak days"
        }

        viewModel.checklistPercent.observe(this) { percent ->
            binding.progressChecklist.progress = percent
            binding.tvChecklistStatus.text = "$percent% Complete"
            binding.tvChecklistStatus.setTextColor(
                if (percent == 100) ContextCompat.getColor(this, R.color.success_green)
                else ContextCompat.getColor(this, R.color.warning_yellow)
            )
        }

        viewModel.allIncidents.observe(this) { incidents ->
            binding.tvIncidentCount.text = "${incidents.size} Near Misses Logged"
        }
    }

    private fun updateTaskDisplay() {
        val task = viewModel.getSelectedTask()
        if (task.isNotEmpty()) {
            binding.tvCurrentTask.text = task
            binding.tvCurrentTask.visibility = View.VISIBLE
            binding.tvNoTask.visibility = View.GONE
        } else {
            binding.tvCurrentTask.visibility = View.GONE
            binding.tvNoTask.visibility = View.VISIBLE
        }
    }

    private fun updateScoreLevel(score: Int) {
        val (level, colorRes) = when {
            score >= 400 -> "SAFETY CHAMPION 🏆" to R.color.gold_color
            score >= 200 -> "SAFETY PRO ⭐" to R.color.success_green
            score >= 100 -> "SAFETY AWARE 👍" to R.color.info_blue
            else -> "BEGINNER 🔰" to R.color.warning_yellow
        }
        binding.tvScoreLevel.text = level
        binding.tvScoreLevel.setTextColor(ContextCompat.getColor(this, colorRes))
    }

    private fun setupClickListeners() {
        // Task Selector / Checklist
        binding.cardTaskSelector.setOnClickListener {
            startActivity(Intent(this, ChecklistActivity::class.java))
        }

        // Risk Meter
        binding.cardRiskMeter.setOnClickListener {
            startActivity(Intent(this, RiskMeterActivity::class.java))
        }

        // Incident Log
        binding.cardIncidentLog.setOnClickListener {
            startActivity(Intent(this, IncidentLogActivity::class.java))
        }

        // Safety Quiz
        binding.cardSafetyQuiz.setOnClickListener {
            startActivity(Intent(this, SafetyQuizActivity::class.java))
        }

        // Safety Score
        binding.cardSafetyScore.setOnClickListener {
            startActivity(Intent(this, SafetyScoreActivity::class.java))
        }

        // Mark Safe Day
        binding.btnMarkSafeDay.setOnClickListener {
            val percent = viewModel.checklistPercent.value ?: 0
            if (percent >= 80) {
                viewModel.completedSafeDay()
                binding.tvSafeDayError.visibility = View.GONE
                binding.tvSafeDaySuccess.visibility = View.VISIBLE
                binding.tvSafeDaySuccess.postDelayed({
                    binding.tvSafeDaySuccess.visibility = View.GONE
                }, 3000)
            } else {
                binding.tvSafeDayError.visibility = View.VISIBLE
            }
        }
    }
}
