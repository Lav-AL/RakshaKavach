package com.rakshakavach.app.ui.riskmeter

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rakshakavach.app.R
import com.rakshakavach.app.data.model.TaskRepository
import com.rakshakavach.app.databinding.ActivityRiskMeterBinding
import com.rakshakavach.app.viewmodel.MainViewModel

class RiskMeterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiskMeterBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiskMeterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        loadRiskInfo()
    }

    private fun loadRiskInfo() {
        val taskName = viewModel.getSelectedTask()
        val task = TaskRepository.tasks.find { it.name == taskName }
        val checklistPercent = viewModel.checklistPercent.value ?: 0

        if (task == null) {
            binding.layoutNoTask.visibility = View.VISIBLE
            binding.layoutRiskInfo.visibility = View.GONE
            return
        }

        binding.layoutNoTask.visibility = View.GONE
        binding.layoutRiskInfo.visibility = View.VISIBLE

        binding.tvCurrentTask.text = "${task.icon} ${task.name}"

        // Calculate effective risk based on gear compliance
        val gearCompliance = checklistPercent / 100f
        val effectiveRisk = when {
            gearCompliance >= 1.0f -> "SAFE"
            gearCompliance >= 0.8f -> "LOW"
            gearCompliance >= 0.5f -> when (task.riskLevel) {
                "EXTREME" -> "EXTREME"
                "HIGH" -> "HIGH"
                else -> "MEDIUM"
            }
            else -> "EXTREME"
        }

        displayRiskLevel(effectiveRisk, task.riskLevel, checklistPercent)
        displayInjuryWarnings(task, checklistPercent)
        displayAvatarStatus(gearCompliance)
    }

    private fun displayRiskLevel(effectiveRisk: String, baseRisk: String, compliance: Int) {
        binding.tvRiskLevel.text = effectiveRisk
        binding.tvGearCompliance.text = "Gear Compliance: $compliance%"

        val (color, bgColor, emoji) = when (effectiveRisk) {
            "SAFE" -> Triple(R.color.success_green, R.color.safe_bg, "✅")
            "LOW" -> Triple(R.color.risk_low, R.color.low_risk_bg, "🟢")
            "MEDIUM" -> Triple(R.color.risk_medium, R.color.medium_risk_bg, "🟡")
            "HIGH" -> Triple(R.color.risk_high, R.color.high_risk_bg, "🟠")
            else -> Triple(R.color.risk_extreme, R.color.extreme_risk_bg, "🔴")
        }

        binding.tvRiskEmoji.text = emoji
        binding.tvRiskLevel.setTextColor(getColor(color))
        binding.layoutRiskBadge.setBackgroundColor(getColor(bgColor))

        // Animate the risk meter bar
        binding.progressRiskMeter.progress = when (effectiveRisk) {
            "SAFE" -> 5
            "LOW" -> 25
            "MEDIUM" -> 50
            "HIGH" -> 75
            else -> 100
        }
    }

    private fun displayInjuryWarnings(task: com.rakshakavach.app.data.model.Task, compliance: Int) {
        val warnings = mutableListOf<String>()

        if (compliance < 100) {
            val missingGearCount = task.requiredGear.size - (compliance * task.requiredGear.size / 100)
            warnings.add("⚠️ $missingGearCount gear items NOT verified!")
        }

        // Task-specific injury warnings
        val taskWarnings = when (task.name) {
            "Welding Work" -> listOf(
                "🔥 Burns from sparks/molten metal",
                "👁️ Eye damage from UV radiation",
                "😮‍💨 Respiratory damage from fumes"
            )
            "Height Work" -> listOf(
                "💀 Fatal fall risk above 6 feet",
                "🦴 Fractures from falling objects",
                "🧠 Head trauma without helmet"
            )
            "Electrical Work" -> listOf(
                "⚡ Electrocution (can be fatal)",
                "🔥 Arc flash burns",
                "💥 Explosion risk"
            )
            "Chemical Handling" -> listOf(
                "☠️ Chemical burns to skin",
                "😮‍💨 Toxic inhalation",
                "👁️ Permanent eye damage"
            )
            "Digging Trench" -> listOf(
                "🏗️ Trench collapse/burial",
                "💀 Head injury from falling debris",
                "🦶 Foot crush injuries"
            )
            else -> listOf(
                "🩹 Laceration/cut injuries",
                "🦴 Crush/impact injuries",
                "😷 Dust/fume inhalation"
            )
        }

        warnings.addAll(if (compliance < 80) taskWarnings else listOf("✅ Risk significantly reduced with proper PPE"))

        binding.tvInjuryWarnings.text = warnings.joinToString("\n")
    }

    private fun displayAvatarStatus(compliance: Float) {
        // Simulated 3D Avatar status based on gear compliance
        binding.tvAvatarStatus.text = when {
            compliance >= 1.0f -> "🦺 FULLY PROTECTED\nWorker is wearing ALL required safety gear"
            compliance >= 0.8f -> "⚠️ MOSTLY PROTECTED\nSome gear items still unchecked"
            compliance >= 0.5f -> "⛔ PARTIALLY PROTECTED\nSignificant gear gaps detected!"
            else -> "❌ DANGEROUSLY UNPROTECTED\nWorker is at EXTREME risk!"
        }

        val avatarColor = when {
            compliance >= 1.0f -> R.color.success_green
            compliance >= 0.8f -> R.color.warning_yellow
            compliance >= 0.5f -> R.color.risk_high
            else -> R.color.risk_extreme
        }
        binding.tvAvatarStatus.setTextColor(getColor(avatarColor))

        // Avatar emoji layers
        val gear = buildString {
            append("👷 Worker Avatar\n\n")
            if (compliance >= 0.8f) append("⛑️ Helmet: ON\n") else append("❌ Helmet: MISSING\n")
            if (compliance >= 0.6f) append("🧤 Gloves: ON\n") else append("❌ Gloves: MISSING\n")
            if (compliance >= 0.4f) append("👢 Boots: ON\n") else append("❌ Boots: MISSING\n")
            if (compliance >= 1.0f) append("🦺 Safety Vest: ON\n") else append("❌ Safety Vest: MISSING\n")
        }
        binding.tvAvatarGear.text = gear
    }
}
