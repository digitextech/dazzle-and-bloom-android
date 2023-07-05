package com.dazzlebloom

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class DazzleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}