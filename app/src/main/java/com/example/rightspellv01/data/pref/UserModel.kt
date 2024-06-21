package com.example.rightspellv01.data.pref


data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
    val password: String,
    val name: String
)