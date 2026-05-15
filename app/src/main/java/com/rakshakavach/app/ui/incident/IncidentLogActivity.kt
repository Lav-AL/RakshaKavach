package com.rakshakavach.app.ui.incident

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rakshakavach.app.R
import com.rakshakavach.app.data.model.IncidentLog
import com.rakshakavach.app.databinding.ActivityIncidentLogBinding
import com.rakshakavach.app.databinding.ItemIncidentBinding
import com.rakshakavach.app.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class IncidentLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncidentLogBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncidentLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupSpinners()
        setupRecyclerView()
        setupObservers()
        setupSubmitButton()
    }

    private fun setupSpinners() {
        val incidentTypes = listOf("Near Miss", "Equipment Failure", "PPE Violation", "Unsafe Condition", "Slip/Trip/Fall", "Other")
        val severities = listOf("LOW", "MEDIUM", "HIGH")

        binding.spinnerIncidentType.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, incidentTypes
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        binding.spinnerSeverity.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, severities
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
    }

    private fun setupRecyclerView() {
        binding.rvIncidents.layoutManager = LinearLayoutManager(this)
        binding.rvIncidents.adapter = IncidentAdapter(emptyList()) { incident ->
            viewModel.deleteIncident(incident.id)
            Toast.makeText(this, "Incident deleted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupObservers() {
        viewModel.allIncidents.observe(this) { incidents ->
            (binding.rvIncidents.adapter as IncidentAdapter).updateList(incidents)
            binding.tvNoIncidents.visibility = if (incidents.isEmpty()) View.VISIBLE else View.GONE
            binding.tvIncidentCount.text = "Total: ${incidents.size} incidents logged"
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmitIncident.setOnClickListener {
            val description = binding.etDescription.text.toString().trim()
            val location = binding.etLocation.text.toString().trim()

            if (description.isEmpty() || location.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val incident = IncidentLog(
                taskName = viewModel.getSelectedTask().ifEmpty { "General" },
                incidentType = binding.spinnerIncidentType.selectedItem.toString(),
                description = description,
                location = location,
                severity = binding.spinnerSeverity.selectedItem.toString()
            )

            viewModel.addIncident(incident)
            binding.etDescription.text?.clear()
            binding.etLocation.text?.clear()
            Toast.makeText(this, "⚠️ Near Miss reported! Staying vigilant saves lives.", Toast.LENGTH_LONG).show()
            binding.layoutForm.visibility = View.GONE
            binding.btnAddIncident.visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
        binding.btnAddIncident.setOnClickListener {
            binding.layoutForm.visibility = View.VISIBLE
            binding.btnAddIncident.visibility = View.GONE
        }
    }

    // Incident Adapter
    class IncidentAdapter(
        private var incidents: List<IncidentLog>,
        private val onDelete: (IncidentLog) -> Unit
    ) : RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {

        class IncidentViewHolder(val binding: ItemIncidentBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
            val binding = ItemIncidentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return IncidentViewHolder(binding)
        }

        override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
            val incident = incidents[position]
            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

            holder.binding.tvIncidentType.text = incident.incidentType
            holder.binding.tvDescription.text = incident.description
            holder.binding.tvLocation.text = "📍 ${incident.location}"
            holder.binding.tvTask.text = "Task: ${incident.taskName}"
            holder.binding.tvTimestamp.text = dateFormat.format(Date(incident.timestamp))

            val severityColor = when (incident.severity) {
                "HIGH" -> R.color.risk_high
                "MEDIUM" -> R.color.risk_medium
                else -> R.color.risk_low
            }
            holder.binding.tvSeverity.text = incident.severity
            holder.binding.tvSeverity.setTextColor(
                holder.itemView.context.getColor(severityColor)
            )

            holder.binding.btnDelete.setOnClickListener { onDelete(incident) }
        }

        override fun getItemCount() = incidents.size

        fun updateList(newList: List<IncidentLog>) {
            incidents = newList
            notifyDataSetChanged()
        }
    }
}
