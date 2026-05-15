package com.rakshakavach.app.ui.checklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rakshakavach.app.R
import com.rakshakavach.app.data.model.Task
import com.rakshakavach.app.data.model.TaskRepository
import com.rakshakavach.app.databinding.ActivityChecklistBinding
import com.rakshakavach.app.databinding.ItemTaskSelectorBinding

class ChecklistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChecklistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChecklistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = TaskAdapter(TaskRepository.tasks) { task ->
            // Open Gear Checklist as a new Activity
            val intent = Intent(this, GearChecklistActivity::class.java)
            intent.putExtra(GearChecklistActivity.EXTRA_TASK_NAME, task.name)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    // ── Task Adapter ──────────────────────────────────────────────────────────
    inner class TaskAdapter(
        private val tasks: List<Task>,
        private val onTaskClick: (Task) -> Unit
    ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

        inner class TaskViewHolder(val binding: ItemTaskSelectorBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val b = ItemTaskSelectorBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return TaskViewHolder(b)
        }

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            val task = tasks[position]
            holder.binding.tvTaskIcon.text = task.icon
            holder.binding.tvTaskName.text = task.name
            holder.binding.tvTaskRisk.text = "${task.riskLevel} RISK"

            holder.binding.tvTaskRisk.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    when (task.riskLevel) {
                        "EXTREME" -> R.color.risk_extreme
                        "HIGH"    -> R.color.risk_high
                        "MEDIUM"  -> R.color.risk_medium
                        else      -> R.color.risk_low
                    }
                )
            )

            holder.binding.root.setOnClickListener {
                onTaskClick(task)
            }
        }

        override fun getItemCount() = tasks.size
    }
}
