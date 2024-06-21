package com.example.rightspellv01.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rightspellv01.data.pref.LoginModel
import com.example.rightspellv01.data.pref.UserModel
import com.example.rightspellv01.data.pref.UserPreference
import com.example.rightspellv01.data.pref.ViewModelFactory
import com.example.rightspellv01.data.pref.dataStore
import com.example.rightspellv01.databinding.ActivityLoginBinding
import com.example.rightspellv01.view.main.MainActivity
import com.example.rightspellv01.view.register.RegisterActivity



@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        val dataStore = UserPreference.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(dataStore, this)
        viewModel = ViewModelProvider(
            this, viewModelFactory
        )[LoginViewModel::class.java]

        viewModel.getSession().observe(this) { user ->
            this.user = user
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.moveActivity.observe(this) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val loginEmail = binding.emailEditText
            val loginPassword = binding.passwordEditText

            when {
                loginEmail.text.toString().isEmpty() -> {
                    loginEmail.error = "Email must be filled in !"
                }

                loginPassword.text.toString().isEmpty() -> {
                    loginPassword.error = "Password must be filled in !"
                }

                else -> {
                    loginEmail.error = null
                    loginPassword.error = null
                }
            }
            if (loginEmail.error == null && loginPassword.error == null) {
                val user = LoginModel(loginEmail.text.toString(), loginPassword.text.toString())
                viewModel.userLogin(user, this)
            }
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun showLoading(it: Boolean) {
        binding.progressBarHome.visibility = if (it) View.VISIBLE else View.GONE
    }
}