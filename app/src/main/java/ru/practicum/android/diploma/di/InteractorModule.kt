package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.VacancyLocalInteractor
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.domain.impl.VacancyLocalInteractorImpl

val interactorModule = module {
    factory<VacancyInteractor> {
        VacancyInteractorImpl(
            repository = get()
        )
    }
    factory<VacancyLocalInteractor> {
        VacancyLocalInteractorImpl(
            repository = get()
        )
    }
}
