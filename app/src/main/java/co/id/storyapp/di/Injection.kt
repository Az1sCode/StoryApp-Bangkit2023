package co.id.storyapp.di

import android.content.Context
import co.id.storyapp.data.UserRepository
import co.id.storyapp.data.api.ApiConfig
import co.id.storyapp.data.pref.UserPreferences
import co.id.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository? {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first().token}
        val apiService = ApiConfig.getApiService(user)
        return UserRepository.getInstance(pref,apiService,true)
    }
}