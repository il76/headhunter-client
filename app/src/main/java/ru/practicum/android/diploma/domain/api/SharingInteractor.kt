package ru.practicum.android.diploma.domain.api

interface SharingInteractor {
    fun share(url: String)
    fun openCallerApp(phone: String)
    fun openEmailApp(email: String)
}
