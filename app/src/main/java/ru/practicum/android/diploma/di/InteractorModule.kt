package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.VacancySearchInteractor
import ru.practicum.android.diploma.domain.impl.VacancySearchInteractorImpl

val interactorModule = module {
    single<VacancySearchInteractor> {
        VacancySearchInteractorImpl(get())
    }
}
