package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.util.Resource

class VacancyViewModel(
    private val networkRepository: VacancyRepository,
    private val localRepository: VacancyLocalRepository
) : ViewModel() {
    private val _screenState = MutableLiveData<VacancyDetailsState>(VacancyDetailsState.LoadingState)
    val screenState: LiveData<VacancyDetailsState> = _screenState
    private var id = -1

    suspend fun loadVacancy(vacancyId: Int, useDB: Boolean = false): Vacancy {
        id = vacancyId
        val vacancyFlow = if (useDB) {
            localRepository.getVacancyDetails(id.toString())
                .map { resource ->
                    when (resource) {
                        is Resource.Success -> resource.data?.let { VacancyDetailsState.ContentState(it) }
                        is Resource.Error -> VacancyDetailsState.NetworkErrorState(resource.message ?: "DB Error")
                    }
                }
        } else {
            networkRepository.getVacancyDetails(vacancyId.toString())
        }

        val state = vacancyFlow.first() // Получаем первый эмиттированный стейт
        return when (state) {
            is VacancyDetailsState.ContentState -> {
                _screenState.value = state
                state.vacancy
            }
            is VacancyDetailsState.EmptyState, null -> throw Exception("Vacancy not found")
            is VacancyDetailsState.NetworkErrorState -> throw Exception(state.message)
            is VacancyDetailsState.ServerError -> throw Exception("Server error")
            is VacancyDetailsState.ConnectionError -> throw Exception("No internet")
            VacancyDetailsState.LoadingState -> throw Exception("Unexpected loading state")
        }
    }
}
