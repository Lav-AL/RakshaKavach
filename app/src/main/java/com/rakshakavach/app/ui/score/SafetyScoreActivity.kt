package com.rakshakavach.app.ui.score

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rakshakavach.app.R
import com.rakshakavach.app.databinding.ActivitySafetyScoreBinding
import com.rakshakavach.app.viewmodel.MainViewModel

class SafetyScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySafetyScoreBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySafetyScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupObservers()
        setupMilestones()
    }

    private fun setupObservers() {
        viewModel.safetyScore.observe(this) { score ->
            binding.tvTotalScore.text = score.toString()
            binding.progressTotalScore.progress = score.coerceAtMost(500)
            binding.tvProgressLabel.text = "$score / 500 points"

            val (level, color, badge) = when {
                score >= 400 -> Triple("SAFETY CHAMPION", R.color.gold_color, "🏆")
                score >= 300 -> Triple("SAFETY MASTER", R.color.success_green, "⭐")
                score >= 200 -> Triple("SAFETY PRO", R.color.info_blue, "🎖️")
                score >= 100 -> Triple("SAFETY AWARE", R.color.warning_yellow, "👍")
                score >= 50  -> Triple("LEARNING", R.color.risk_medium, "📚")
                else -> Triple("BEGINNER", R.color.risk_low, "🔰")
            }
            binding.tvCurrentLevel.text = "$badge $level"
            binding.tvCurrentLevel.setTextColor(getColor(color))

            // Next level info
            val nextTarget = when {
                score >= 400 -> null
                score >= 300 -> 400
                score >= 200 -> 300
                score >= 100 -> 200
                score >= 50 -> 100
                else -> 50
            }
            if (nextTarget != null) {
                binding.tvNextLevel.text = "${nextTarget - score} points to next level"
                binding.progressToNextLevel.max = nextTarget
                binding.progressToNextLevel.progress = score
            } else {
                binding.tvNextLevel.text = "🎉 Maximum level achieved!"
                binding.progressToNextLevel.progress = 100
            }
        }

        viewModel.streak.observe(this) { streak ->
            binding.tvStreakCount.text = "$streak"
            binding.tvStreakLabel.text = if (streak == 1) "Consecutive Safe Day" else "Consecutive Safe Days"

            val streakMessage = when {
                streak >= 30 -> "🔥 INCREDIBLE! One Month Streak!"
                streak >= 14 -> "💪 OUTSTANDING! Two Week Streak!"
                streak >= 7 -> "⭐ GREAT! One Week Streak!"
                streak >= 3 -> "👍 GOOD! Keep it up!"
                streak > 0 -> "🌟 Good Start! Maintain your streak!"
                else -> "Start your safe day streak!"
            }
            binding.tvStreakMessage.text = streakMessage
        }
    }

    private fun setupMilestones() {
        val milestones = listOf(
            Triple("Complete Safety Checklist", "+10 pts/day", "✅"),
            Triple("Perfect Quiz Score", "+30 pts", "🎯"),
            Triple("3-Day Safe Streak", "+20 pts", "🔥"),
            Triple("7-Day Safe Streak", "+50 pts", "⭐"),
            Triple("Report Near Miss", "-20 pts (learn from it)", "⚠️"),
            Triple("Daily Login", "+5 pts", "📅")
        )

        val milestoneText = milestones.joinToString("\n") { (action, points, icon) ->
            "$icon  $action — $points"
        }
        binding.tvMilestones.text = milestoneText
    }
}
