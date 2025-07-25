package ru.practicum.android.diploma.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.ui.favorite.FavoriteViewModel
import ru.practicum.android.diploma.ui.filter.FilterViewModel
import ru.practicum.android.diploma.ui.search.SearchViewModel
import ru.practicum.android.diploma.ui.vacancy.VacancyViewModel

val viewModelModule = module {

    viewModel {
        FavoriteViewModel()
    }

    viewModel {
        FilterViewModel()
    }

    viewModel {
        SearchViewModel(
            repository = get()
        )
    }

    viewModel {
        VacancyViewModel()
    }
}
