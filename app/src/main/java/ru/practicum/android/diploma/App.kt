package ru.practicum.android.diploma

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        switchTheme(CurrentTheme.isDarkTheme(this))
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
