package ru.practicum.android.diploma.ui.sharing

interface ExternalNavigator {
    fun shareLink(link: String)
    fun openEmailApp(email: String)
    fun openCallerApp(phone: String)
}
