package ru.practicum.android.diploma.ui.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

    // Добавляем LiveData для флага избранного
    private val _isFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var id = -1
    private var loadedFromNetwork = false // Флаг, откуда загружены данные

    suspend fun loadVacancy(vacancyId: Int, useDB: Boolean = false): Vacancy {
        id = vacancyId
        loadedFromNetwork = !useDB // Запоминаем, откуда загружаем

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
                // Если загрузили из сети - проверяем наличие в БД
                if (loadedFromNetwork) {
                    checkFavoriteStatus(state.vacancy.id)
                } else {
                    // Если загрузили из БД - значит точно в избранном
                    _isFavorite.value = true
                }
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

    // Метод для проверки наличия вакансии в избранном
    private suspend fun checkFavoriteStatus(vacancyId: String) {
        val vacancy = localRepository.getVacancyDetails(vacancyId).first()
        _isFavorite.value = vacancy is Resource.Success && vacancy.data != null
    }

    fun shareVacancy(url: String) {
        sharingInteractor.share(url)
    }

    suspend fun saveFavoriteVacancy() {
        when (val currentState = screenState.value) {
            is VacancyDetailsState.ContentState -> {
                localRepository.saveVacancy(currentState.vacancy)
                _isFavorite.value = true // Обновляем флаг после сохранения
            }
            else -> {
                throw VacancyErrorException("No vacancy data to save")
            }
        }
    }

    suspend fun deleteFavoriteVacancy() {
        when (val currentState = screenState.value) {
            is VacancyDetailsState.ContentState -> {
                localRepository.deleteVacancy(currentState.vacancy.id)
                _isFavorite.value = false // Обновляем флаг после удаления
            }
            else -> {
                throw VacancyErrorException("No vacancy data to save")
            }
        }
    }

    // Новый метод для загрузки избранной вакансии
    suspend fun loadFavoriteVacancy(vacancyId: String) {
        try {
            val result = localRepository.getVacancyDetails(vacancyId).first()
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        _screenState.value = VacancyDetailsState.ContentState(it)
                        _isFavorite.value = true
                    } ?: run {
                        _screenState.value = VacancyDetailsState.EmptyState
                        _isFavorite.value = false
                    }
                }
                is Resource.Error -> {
                    _screenState.value = VacancyDetailsState.NetworkErrorState(result.message ?: "DB Error")
                    _isFavorite.value = false
                }
            }
        } catch (e: VacancyErrorException) {
            _screenState.value = VacancyDetailsState.NetworkErrorState(e.message ?: "Error loading favorite")
            _isFavorite.value = false
        }
    }

    fun favoriteAction() {
        when (val currentState = screenState.value) {
            is VacancyDetailsState.ContentState -> {
                val isFavorite = _isFavorite.value ?: false
                viewModelScope.launch {
                    if (isFavorite) {
                        deleteFavoriteVacancy() // Удаляем, если уже в избранном
                    } else {
                        saveFavoriteVacancy() // Добавляем, если не в избранном
                    }
                }
            }
            else -> {
                throw VacancyErrorException("No vacancy data to perform favorite action")
            }
        }
    }
}

class VacancyErrorException(message: String) : Exception(message)
