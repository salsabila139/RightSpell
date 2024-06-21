package com.example.rightspellv01.view.main


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rightspellv01.R
import com.example.rightspellv01.data.pref.UserPreference
import com.example.rightspellv01.data.pref.ViewModelFactory
import com.example.rightspellv01.data.pref.dataStore
import com.example.rightspellv01.databinding.ActivityMainBinding
import com.example.rightspellv01.view.login.LoginActivity
import com.example.rightspellv01.view.main.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigation: BottomNavigationView = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.to_home
        setupNavigation(bottomNavigation)

        val dataStore = UserPreference.getInstance(dataStore)

        setupViewModel(dataStore)
        setupBackButton()
    }

    @Suppress("DEPRECATION")
    private fun setupBackButton() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExit) {
                    finishAffinity()
                } else {
                    doubleBackToExit = true
                    Toast.makeText(
                        this@MainActivity, "Press back again to exit", Toast.LENGTH_SHORT
                    )
                        .show()
                    Handler().postDelayed({
                        doubleBackToExit = false
                    },2000)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setupViewModel(dataStore: UserPreference) {
        val viewModelFactory = ViewModelFactory(dataStore, this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.loading.observe(this) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarHome.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun setupNavigation(iconNavigation: BottomNavigationView) {
        iconNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.to_home -> {
                    false
                }

                R.id.to_profile -> {
                    false
                }

                R.id.logout -> {
                    android.app.AlertDialog.Builder(this).apply {
                        setTitle("LogOut")
                        setMessage("Are you sure want to logout?")
                        setPositiveButton("Yes") { _, _ ->
                            viewModel.logout()
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        setNegativeButton("No") { _, _ ->

                        }
                        create()
                        show()
                    }
                    return@setOnItemSelectedListener true
                }

                else -> false
            }
        }
    }
}