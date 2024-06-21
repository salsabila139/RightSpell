package com.example.rightspellv01.view.login

import ApiConfig
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.rightspellv01.data.pref.LoginModel
import com.example.rightspellv01.data.pref.UserModel
import com.example.rightspellv01.data.pref.UserPreference
import com.example.rightspellv01.data.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val pref: UserPreference) : ViewModel() {

    private val apiService = ApiConfig.getApiService()
    private val _moveActivity = MutableLiveData<Unit>()
    val moveActivity: LiveData<Unit> = _moveActivity

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun userLogin(user: LoginModel, context: Context) {
        _isLoading.value = true
        val client = apiService.userLogin(user)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val responseCode = response.code()

                    if (responseBody != null) {
                        isLogin(responseBody.token)
                        resultDialog(
                            "Login Successful",
                            "Login successful, press next to continue !",
                            context
                        )
                    } else {
                        resultDialog("login failed", "login failed, please try again!", context)
                    }

                } else if (response.code() == 403 ) {
                    resultDialog("Login Failed !", "password is wrong", context)

                } else if (response.code() == 404 ) {
                    resultDialog("Login Failed !", "user is not found", context)

                } else {
                    resultDialog(
                        "Register Failed", "Internal Server Error", context
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                resultDialog("Loggin Failed !", t.message, context)
            }
        })
    }

    private fun isLogin(token: String) {
        viewModelScope.launch {
            pref.login(token)
        }
    }

    private fun resultDialog(title: String, message: String?, context: Context) {
        if (title == "Login Failed !") {
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