package com.example.rightspellv01.view.register

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.rightspellv01.data.pref.LoginModel
import com.example.rightspellv01.data.pref.RegisterModel
import com.example.rightspellv01.data.pref.UserModel
import com.example.rightspellv01.data.pref.UserPreference
import com.example.rightspellv01.data.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (private val pref: UserPreference) : ViewModel() {

    private val apiService = ApiConfig.getApiService()
    private val _moveActivity = MutableLiveData<Unit>()
    val moveActivity: LiveData<Unit> = _moveActivity

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun userRegister(user: RegisterModel, context: Context) {
        _isLoading.value = true
        val client = apiService.userRegister(user)
        client.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _isLoading.value = false
                val responseCode = response.code()

                if (responseCode == 201) {
                    resultDialog(
                        "Register Successful", "Register Successful, press next to continue!", context
                    )
                } else if (responseCode == 400) {
                    resultDialog(
                        "Register Failed", "Register failed, input invalid", context
                    )
                } else {
                    resultDialog(
                        "Register Failed", "Internal Server Error", context
                    )
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _isLoading.value = false
                resultDialog("Register Failed", t.message, context)
            }
        })
    }

    private fun isLogin(token: String) {
        viewModelScope.launch {
            pref.login(token)
        }
    }

    private fun resultDialog(title: String, message: String?, context: Context) {
        if (title == "Register Failed") {
            AlertDialog.Builder(context).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton("Retry") { _, _ ->
                }
                setCancelable(false)
                create()
                show()
            }
        } else {
            AlertDialog.Builder(context).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton("Next") { _, _ ->
                    _moveActivity.value = Unit
                }
                setCancelable(false)
                create()
                show()
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return pref.getSession().asLiveData()
    }
}