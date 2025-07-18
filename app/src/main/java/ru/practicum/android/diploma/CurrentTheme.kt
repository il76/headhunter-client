package ru.practicum.android.diploma

import android.content.Context
import android.content.res.Configuration

object CurrentTheme {
    fun isDarkTheme(context: Context): Boolean {
        val mode = context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
        when (mode) {
            Configuration.UI_MODE_NIGHT_YES -> { return true }
            else -> { return false }
        }
    }

}
