package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.favorite.FavoriteViewModel
import ru.practicum.android.diploma.ui.filter.FilterViewModel
import ru.practicum.android.diploma.ui.filter.IndustriesViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyViewModel

val viewModelModule = module {

    viewModel {
        FavoriteViewModel(
            repository = get()
        )
    }

    viewModel {
        FilterViewModel(
            sharedPrefInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(
            repository = get(),
            sharedPrefInteractor = get()
        )
    }

    viewModel {
        VacancyViewModel(
            networkRepository = get(),
            localRepository = get(),
            sharingInteractor = get(),
        )
    }

    viewModel {
        IndustriesViewModel(
            get()
        )
    }
}
