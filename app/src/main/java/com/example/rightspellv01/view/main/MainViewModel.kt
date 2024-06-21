package com.example.rightspellv01.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.rightspellv01.data.pref.UserModel
import com.example.rightspellv01.data.pref.UserPreference
import com.example.rightspellv01.data.repo.StoryRepo
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (private val pref: UserPreference?, private val storyRepo: StoryRepo) :
    ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    fun setToken(token: String) {
        storyRepo.setToken(token)
    }

    fun getSession(): LiveData<UserModel>? {
        return pref?.getSession()?.asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref?.logout()
        }
    }
}