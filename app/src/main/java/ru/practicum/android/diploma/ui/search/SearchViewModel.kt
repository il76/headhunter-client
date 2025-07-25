package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.VacancySearchInteractor

class SearchViewModel(
    private val interactor: VacancySearchInteractor
) : ViewModel() {
    fun searchRequest(searchText: String) {
        viewModelScope.launch {
            interactor
                .searchVacancies(searchText)
                .collect {
                    it.first?.forEach { vacancy ->
                        println("receive: ${vacancy.name}")
                    }
                }
        }
    }
}
