package com.rakshakavach.app.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rakshakavach.app.R
import com.rakshakavach.app.databinding.ActivityRegisterBinding
import com.rakshakavach.app.ui.home.MainActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardForm.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnRegister.setOnClickListener {
            val name     = binding.etName.text?.toString()?.trim() ?: ""
            val email    = binding.etEmail.text?.toString()?.trim() ?: ""
            val phone    = binding.etPhone.text?.toString()?.trim() ?: ""
            val role     = binding.etRole.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString() ?: ""
            val confirm  = binding.etConfirmPassword.text?.toString() ?: ""

            when {
                name.isEmpty()    -> showError("Please enter your full name")
                email.isEmpty()   -> showError("Please enter your email")
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    showError("Please enter a valid email")
                phone.isEmpty()   -> showError("Please enter your phone number")
                phone.length < 10 -> showError("Enter a valid 10-digit phone number")
                password.isEmpty()  -> showError("Please enter a password")
                password.length < 6 -> showError("Password must be at least 6 characters")
                confirm.isEmpty()   -> showError("Please confirm your password")
                password != confirm -> showError("Passwords do not match")
                else -> attemptRegister(name, email, phone, role, password)
            }
        }

        binding.tvLoginLink.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun attemptRegister(
        name: String, email: String, phone: String, role: String, password: String
    ) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.isEnabled = false

        binding.root.postDelayed({
            val prefs = getSharedPreferences("raksha_auth", Context.MODE_PRIVATE)

            // Check if email already registered
            if (prefs.getString("user_email", null) == email) {
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.isEnabled = true
                showError("This email is already registered. Please login.")
                return@postDelayed
            }

            // Save credentials
            prefs.edit()
                .putString("user_name", name)
                .putString("user_email", email)
                .putString("user_phone", phone)
                .putString("user_role", role.ifEmpty { "Construction Worker" })
                .putString("user_password", password)
                .putBoolean("is_logged_in", true)
                .apply()

            binding.progressBar.visibility = View.GONE

            Toast.makeText(
                this,
                "Welcome to Raksha-Kavach, $name! 🛡️ Stay Safe!",
                Toast.LENGTH_LONG
            ).show()

            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finishAffinity() // Clear back stack
        }, 1000)
    }

    private fun showError(msg: String) {
        binding.tvError.text = msg
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
    }
}
