package com.example.rightspellv01.data.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)

data class LoginResponse(
    @SerializedName("data")
    val data: LoginResult,
    @SerializedName("token")
    val token: String,
)

data class LoginResult(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
)