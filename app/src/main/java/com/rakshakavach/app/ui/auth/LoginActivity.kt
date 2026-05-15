package com.rakshakavach.app.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rakshakavach.app.R
import com.rakshakavach.app.databinding.ActivityLoginBinding
import com.rakshakavach.app.ui.home.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateViews()
        setupClickListeners()
    }

    private fun animateViews() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.ivLogo.startAnimation(fadeIn)
        binding.tvTitle.startAnimation(slideUp)
        binding.tvSubtitle.startAnimation(slideUp)
        binding.cardForm.startAnimation(slideUp)
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString() ?: ""

            when {
                email.isEmpty() -> showError("Please enter your email")
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    showError("Please enter a valid email")
                password.isEmpty() -> showError("Please enter your password")
                password.length < 6 -> showError("Password must be at least 6 characters")
                else -> attemptLogin(email, password)
            }
        }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_SHORT).show()
        }
    }

    private fun attemptLogin(email: String, password: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false

        // Simulate async login — check against stored credentials
        binding.root.postDelayed({
            val prefs = getSharedPreferences("raksha_auth", Context.MODE_PRIVATE)
            val storedEmail = prefs.getString("user_email", null)
            val storedPassword = prefs.getString("user_password", null)

            binding.progressBar.visibility = View.GONE
            binding.btnLogin.isEnabled = true

            when {
                storedEmail == null -> showError("No account found. Please register first.")
                email != storedEmail -> showError("Incorrect email address")
                password != storedPassword -> showError("Incorrect password")
                else -> {
                    // Save session
                    prefs.edit().putBoolean("is_logged_in", true).apply()
                    Toast.makeText(
                        this,
                        "Welcome back, ${prefs.getString("user_name", "Worker")}! 🛡️",
                        Toast.LENGTH_SHORT
                    ).show()
                    goToHome()
                }
            }
        }, 800)
    }

    private fun showError(msg: String) {
        binding.tvError.text = msg
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
