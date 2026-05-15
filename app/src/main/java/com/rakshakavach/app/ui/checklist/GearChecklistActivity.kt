package com.rakshakavach.app.ui.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rakshakavach.app.R
import com.rakshakavach.app.data.model.TaskRepository
import com.rakshakavach.app.databinding.ActivityGearChecklistBinding
import com.rakshakavach.app.databinding.ItemGearCheckBinding
import com.rakshakavach.app.viewmodel.MainViewModel

class GearChecklistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGearChecklistBinding
    private val viewModel: MainViewModel by viewModels()
    private val checkedGear = mutableSetOf<String>()

    companion object {
        const val EXTRA_TASK_NAME = "extra_task_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGearChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        val taskName = intent.getStringExtra(EXTRA_TASK_NAME) ?: ""
        val task = TaskRepository.tasks.find { it.name == taskName }

        if (task == null) {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Save selected task
        viewModel.setSelectedTask(task.name)

        // Header info
        binding.tvTaskName.text = "${task.icon}  ${task.name}"
        binding.tvTaskDescription.text = task.description

        // Risk chip color
        binding.chipRisk.text = "${task.riskLevel} RISK"
        binding.chipRisk.setChipBackgroundColorResource(
            when (task.riskLevel) {
                "EXTREME" -> R.color.risk_extreme
                "HIGH"    -> R.color.risk_high
                "MEDIUM"  -> R.color.risk_medium
                else      -> R.color.risk_low
            }
        )

        // Required gear count badge
        binding.tvGearCount.text = "${task.requiredGear.size} items required"

        // Setup RecyclerView
        binding.rvGearChecklist.layoutManager = LinearLayoutManager(this)
        binding.rvGearChecklist.adapter = GearCheckAdapter(task.requiredGear) { gear, isChecked ->
            if (isChecked) checkedGear.add(gear) else checkedGear.remove(gear)
            updateProgress(task.requiredGear.size)
        }

        // Done button
        binding.btnDone.setOnClickListener {
            val percent = viewModel.checklistPercent.value ?: 0
            if (percent == 100) {
                Toast.makeText(this, "✅ All gear verified! Stay Safe!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "⚠️ Please check all gear items first!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateProgress(total: Int) {
        val checked = checkedGear.size
        val percent = ((checked.toFloat() / total) * 100).toInt()

        binding.progressGear.progress = percent
        binding.tvGearProgress.text = "$checked / $total Gear Items Verified"
        viewModel.updateChecklistPercent(percent)

        when {
            percent == 100 -> {
                binding.tvStatusMessage.text = "✅ ALL GEAR CHECKED! You are SAFE to work!"
                binding.tvStatusMessage.setTextColor(ContextCompat.getColor(this, R.color.success_green))
                binding.tvStatusMessage.setBackgroundColor(0x1A00E676.toInt())
                binding.btnDone.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.success_green)
                binding.btnDone.text = "✅ ALL CLEAR — PROCEED TO WORK"
            }
            percent >= 80 -> {
                binding.tvStatusMessage.text = "⚠️ Almost done — check remaining items"
                binding.tvStatusMessage.setTextColor(ContextCompat.getColor(this, R.color.warning_yellow))
                binding.tvStatusMessage.setBackgroundColor(0x1AFFD600.toInt())
            }
            else -> {
                binding.tvStatusMessage.text = "❌ ${total - checked} gear items still unchecked!"
                binding.tvStatusMessage.setTextColor(ContextCompat.getColor(this, R.color.risk_high))
                binding.tvStatusMessage.setBackgroundColor(0x1AFF6D00.toInt())
            }
        }
    }

    // ── Gear Adapter ──────────────────────────────────────────────────────────
    inner class GearCheckAdapter(
        private val gearList: List<String>,
        private val onCheckChange: (String, Boolean) -> Unit
    ) : RecyclerView.Adapter<GearCheckAdapter.GearViewHolder>() {

        inner class GearViewHolder(val binding: ItemGearCheckBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GearViewHolder {
            val b = ItemGearCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GearViewHolder(b)
        }

        override fun onBindViewHolder(holder: GearViewHolder, position: Int) {
            val gear = gearList[position]
            holder.binding.tvGearName.text = gear
            holder.binding.tvGearIcon.text = getGearIcon(gear)
            holder.binding.checkboxGear.isChecked = false

            holder.binding.checkboxGear.setOnCheckedChangeListener { _, isChecked ->
                onCheckChange(gear, isChecked)
                holder.binding.root.alpha       = if (isChecked) 0.6f else 1.0f
                holder.binding.tvGearName.paint.isStrikeThruText = isChecked
                holder.binding.tvGearName.invalidate()
            }
        }

        override fun getItemCount() = gearList.size

        private fun getGearIcon(gear: String) = when {
            gear.contains("Helmet", true)                               -> "⛑️"
            gear.contains("Gloves", true)                              -> "🧤"
            gear.contains("Boots", true)                               -> "👢"
            gear.contains("Harness", true)                             -> "🔗"
            gear.contains("Goggles", true) || gear.contains("Shield", true) -> "🥽"
            gear.contains("Vest", true)                                -> "🦺"
            gear.contains("Respirator", true) || gear.contains("Breathing", true) -> "😷"
            gear.contains("Ear", true)                                 -> "🎧"
            gear.contains("Belt", true) || gear.contains("Back", true) || gear.contains("Knee", true) -> "🔒"
            gear.contains("Suit", true)                                -> "👔"
            else                                                       -> "🛡️"
        }
    }
}
