package com.example.rightspellv01.data.repo

import ApiService

class StoryRepo (private val apiService: ApiService) {

    private var token = ""

    fun setToken(token: String) {
        this.token = token
    }
}