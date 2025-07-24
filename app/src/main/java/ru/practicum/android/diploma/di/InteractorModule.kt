package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.impl.VacancyInteractorImpl

val interactorModule = module {
    factory<VacancyInteractor> {
        VacancyInteractorImpl(
            repository = get()
        )
    }
}
