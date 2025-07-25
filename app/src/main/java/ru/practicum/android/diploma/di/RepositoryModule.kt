package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.VacancyRepository
import ru.practicum.android.diploma.data.VacancyRepositoryImpl

val repositoryModule = module {
    single<VacancyRepository> {
        VacancyRepositoryImpl(get())
    }
}
