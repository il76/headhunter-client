package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.IndustriesRepositoryImpl
import ru.practicum.android.diploma.data.SharedPrefRepositoryImpl
import ru.practicum.android.diploma.data.VacancyLocalRepositoryImpl
import ru.practicum.android.diploma.data.VacancyRepositoryImpl
import ru.practicum.android.diploma.data.db.VacancyDbConverter
import ru.practicum.android.diploma.domain.api.IndustriesRepository
import ru.practicum.android.diploma.domain.api.SharedPrefRepository
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.api.VacancyRepository

val repositoryModule = module {
    factory<VacancyRepository> {
        VacancyRepositoryImpl(
            networkClient = get()
        )
    }
    factory<VacancyLocalRepository> {
        VacancyLocalRepositoryImpl(
            appDatabase = get(),
            vacancyDbConverter = get()
        )
    }
    factory { VacancyDbConverter(get()) }

    factory<IndustriesRepository> {
        IndustriesRepositoryImpl(
            networkClient = get()
        )
    }

    single<SharedPrefRepository> {
        SharedPrefRepositoryImpl(
            sharedPreferences = get(),
            gson = get()
        )
    }
}
