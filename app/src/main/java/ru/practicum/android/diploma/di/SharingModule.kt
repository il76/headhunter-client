package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.ui.sharing.ExternalNavigator
import ru.practicum.android.diploma.ui.sharing.ExternalNavigatorImpl

val sharingModule = module {
    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
}
