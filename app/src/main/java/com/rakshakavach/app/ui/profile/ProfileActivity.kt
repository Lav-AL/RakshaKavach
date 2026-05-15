package com.rakshakavach.app.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rakshakavach.app.R
import com.rakshakavach.app.databinding.ActivityProfileBinding
import com.rakshakavach.app.viewmodel.MainViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel: MainViewModel by viewModels()
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        loadProfile()
        setupStats()
        setupEditToggle()
    }

    private fun loadProfile() {
        val prefs = getSharedPreferences("raksha_auth", Context.MODE_PRIVATE)
        val name  = prefs.getString("user_name", "") ?: ""
        val email = prefs.getString("user_email", "") ?: ""
        val phone = prefs.getString("user_phone", "") ?: ""
        val role  = prefs.getString("user_role", "") ?: ""

        // Extended profile fields
        val company    = prefs.getString("user_company", "") ?: ""
        val site       = prefs.getString("user_site", "") ?: ""
        val experience = prefs.getString("user_experience", "") ?: ""
        val emergency  = prefs.getString("user_emergency", "") ?: ""
        val certifications = prefs.getString("user_certifications", "") ?: ""
        val bloodGroup = prefs.getString("user_blood", "") ?: ""
        val bio        = prefs.getString("user_bio", "") ?: ""

        // Avatar initials
        val initials = name.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
        binding.tvAvatarInitials.text = initials.ifEmpty { "?" }

        // Basic info
        binding.tvDisplayName.text = name.ifEmpty { "Set your name" }
        binding.tvDisplayRole.text = role.ifEmpty { "Construction Worker" }

        // Editable fields
        binding.etName.setText(name)
        binding.etEmail.setText(email)
        binding.etPhone.setText(phone)
        binding.etRole.setText(role)
        binding.etCompany.setText(company)
        binding.etSite.setText(site)
        binding.etExperience.setText(experience)
        binding.etEmergency.setText(emergency)
        binding.etCertifications.setText(certifications)
        binding.etBloodGroup.setText(bloodGroup)
        binding.etBio.setText(bio)

        setFieldsEditable(false)
    }

    private fun setupStats() {
        val score   = viewModel.safetyScore.value ?: 0
        val streak  = viewModel.streak.value ?: 0
        val prefs   = getSharedPreferences("raksha_prefs", Context.MODE_PRIVATE)
        val quizzes = prefs.getInt("quizzes_taken", 0)

        viewModel.safetyScore.observe(this) { s ->
            binding.tvStatScore.text = s.toString()
        }
        viewModel.streak.observe(this) { st ->
            binding.tvStatStreak.text = "$st"
        }
        viewModel.allIncidents.observe(this) { incidents ->
            binding.tvStatIncidents.text = incidents.size.toString()
        }

        binding.tvStatScore.text = score.toString()
        binding.tvStatStreak.text = "$streak"
        binding.tvStatQuizzes.text = quizzes.toString()

        // Safety level badge
        val (level, colorRes) = when {
            score >= 400 -> "SAFETY CHAMPION 🏆" to R.color.gold_color
            score >= 200 -> "SAFETY PRO ⭐"      to R.color.success_green
            score >= 100 -> "SAFETY AWARE 👍"    to R.color.info_blue
            else         -> "BEGINNER 🔰"         to R.color.warning_yellow
        }
        binding.tvSafetyLevel.text = level
        binding.tvSafetyLevel.setTextColor(ContextCompat.getColor(this, colorRes))
    }

    private fun setupEditToggle() {
        binding.btnEdit.setOnClickListener {
            if (!isEditing) {
                // Enter edit mode
                isEditing = true
                setFieldsEditable(true)
                binding.btnEdit.text = "💾  SAVE PROFILE"
                binding.btnEdit.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.success_green)
                binding.tvEditHint.visibility = View.VISIBLE
            } else {
                // Save
                saveProfile()
            }
        }
    }

    private fun saveProfile() {
        val name  = binding.etName.text?.toString()?.trim() ?: ""
        val phone = binding.etPhone.text?.toString()?.trim() ?: ""
        val role  = binding.etRole.text?.toString()?.trim() ?: ""

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        getSharedPreferences("raksha_auth", Context.MODE_PRIVATE).edit().apply {
            putString("user_name", name)
            putString("user_phone", phone)
            putString("user_role", role.ifEmpty { "Construction Worker" })
            putString("user_company",       binding.etCompany.text?.toString()?.trim() ?: "")
            putString("user_site",          binding.etSite.text?.toString()?.trim() ?: "")
            putString("user_experience",    binding.etExperience.text?.toString()?.trim() ?: "")
            putString("user_emergency",     binding.etEmergency.text?.toString()?.trim() ?: "")
            putString("user_certifications",binding.etCertifications.text?.toString()?.trim() ?: "")
            putString("user_blood",         binding.etBloodGroup.text?.toString()?.trim() ?: "")
            putString("user_bio",           binding.etBio.text?.toString()?.trim() ?: "")
            apply()
        }

        // Update display
        val initials = name.split(" ")
            .filter { it.isNotEmpty() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
        binding.tvAvatarInitials.text = initials.ifEmpty { "?" }
        binding.tvDisplayName.text = name
        binding.tvDisplayRole.text = role.ifEmpty { "Construction Worker" }

        isEditing = false
        setFieldsEditable(false)
        binding.btnEdit.text = "✏️  EDIT PROFILE"
        binding.btnEdit.backgroundTintList =
            ContextCompat.getColorStateList(this, R.color.warning_yellow)
        binding.tvEditHint.visibility = View.GONE

        Toast.makeText(this, "✅ Profile saved successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun setFieldsEditable(editable: Boolean) {
        val fields = listOf(
            binding.etName, binding.etPhone, binding.etRole,
            binding.etCompany, binding.etSite, binding.etExperience,
            binding.etEmergency, binding.etCertifications,
            binding.etBloodGroup, binding.etBio
        )
        fields.forEach { et ->
            et.isEnabled = editable
            et.alpha = if (editable) 1.0f else 0.75f
        }
        // Email is never editable (it's the account ID)
        binding.etEmail.isEnabled = false
        binding.etEmail.alpha = 0.5f
    }
}
