package com.example.rightspellv01.di

import android.content.Context
import com.example.rightspellv01.data.repo.StoryRepo

object Injection {
    fun supplyRepository(context: Context): StoryRepo {
        val apiService = ApiConfig.getApiService()
        return StoryRepo(apiService)
    }
}