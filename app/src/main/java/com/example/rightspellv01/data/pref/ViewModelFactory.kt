package com.example.rightspellv01.data.pref

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rightspellv01.di.Injection
import com.example.rightspellv01.view.login.LoginViewModel
import com.example.rightspellv01.view.main.MainViewModel
import com.example.rightspellv01.view.register.RegisterViewModel

class ViewModelFactory (private val pref: UserPreference, private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref, Injection.supplyRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}