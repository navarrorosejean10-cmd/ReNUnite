package com.example.renunite

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class ReNUniteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Load the saved theme preference
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("DarkMode", false)
        
        // Force Light Mode (White Mode) as the standard unless Dark Mode is explicitly enabled
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}
