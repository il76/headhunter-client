package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.api.SharingInteractor
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.util.Resource

class VacancyViewModel(
    private val networkRepository: VacancyRepository,
    private val localRepository: VacancyLocalRepository,
    private val sharingInteractor: SharingInteractor,
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

        val state = vacancyFlow.first()
        return when (state) {
            is VacancyDetailsState.ContentState -> {
                _screenState.value = state
                state.vacancy
            }
            else -> throw when (state) {
                is VacancyDetailsState.EmptyState -> VacancyErrorException("Vacancy not found")
                is VacancyDetailsState.NetworkErrorState -> VacancyErrorException(state.message)
                is VacancyDetailsState.ServerError -> VacancyErrorException("Server error")
                is VacancyDetailsState.ConnectionError -> VacancyErrorException("No internet")
                VacancyDetailsState.LoadingState -> VacancyErrorException("Unexpected loading state")
                else -> VacancyErrorException("Unknown error")
            }
        }
    }

    fun shareVacancy(url: String) {
        sharingInteractor.share(url)
    }
}

class VacancyErrorException(message: String) : Exception(message)
