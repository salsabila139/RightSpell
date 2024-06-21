package com.example.rightspellv01.view.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rightspellv01.data.pref.LoginModel
import com.example.rightspellv01.data.pref.RegisterModel
import com.example.rightspellv01.data.pref.UserModel
import com.example.rightspellv01.data.pref.UserPreference
import com.example.rightspellv01.data.pref.ViewModelFactory
import com.example.rightspellv01.data.pref.dataStore
import com.example.rightspellv01.databinding.ActivityRegisterBinding
import com.example.rightspellv01.view.login.LoginActivity
import com.example.rightspellv01.view.main.MainActivity

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        val dataStore = UserPreference.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(dataStore, this)
        viewModel = ViewModelProvider(
            this, viewModelFactory
        )[RegisterViewModel::class.java]

        viewModel.getSession().observe(this) { user ->
            this.user = user
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.moveActivity.observe(this) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val registerName = binding.nameEditText
            val registerEmail = binding.emailEditText
            val registerPassword = binding.passwordEditText
            val registerConfirmPassword = binding.confirmPasswordEditText

            when {

                registerName.text.toString().isEmpty() -> {
                    registerName.error = "Name must be filled in !"
                }

                registerEmail.text.toString().isEmpty() -> {
                    registerEmail.error = "Email must be filled in !"
                }

                registerPassword.text.toString().isEmpty() -> {
                    registerPassword.error = "Password must be filled in !"
                }

                registerConfirmPassword.text.toString().isEmpty() -> {
                    registerConfirmPassword.error = "Confirm password must be filled in !"
                }

                registerConfirmPassword.text.toString() != registerPassword.text.toString() -> {
                    registerConfirmPassword.error = "Confirm password must be same as password !"
                }

                else -> {
                    registerName.error = null
                    registerEmail.error = null
                    registerPassword.error = null
                    registerConfirmPassword.error =null
                }
            }
            if (registerName.error == null && registerEmail.error == null && registerPassword.error == null && registerConfirmPassword.error == null) {
                val user = RegisterModel(registerName.text.toString(), registerEmail.text.toString(), registerPassword.text.toString())
                viewModel.userRegister(user, this)
            }
        }

        binding.btnSignin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun showLoading(it: Boolean) {
        binding.progressBarHome.visibility = if (it) View.VISIBLE else View.GONE
    }
}