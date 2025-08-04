package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.ui.sharing.ExternalNavigator

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun share(url: String) {
        externalNavigator.shareLink(url)
    }
}
