package ru.practicum.android.diploma.ui.sharing

import android.content.Context
import android.content.Intent

class ExternalNavigator(private val context: Context) {
    fun shareLink(link: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        context.startActivity(shareIntent)
    }
}
