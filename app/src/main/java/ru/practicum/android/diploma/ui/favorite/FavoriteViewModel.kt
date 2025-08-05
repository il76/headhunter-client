package ru.practicum.android.diploma.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class FavoriteViewModel(private val repository: VacancyLocalRepository) : ViewModel() {
    private val _state = MutableStateFlow<FavoriteUIState>(FavoriteUIState())
    val state: StateFlow<FavoriteUIState> = _state.asStateFlow()

    fun search() {
        _state.update {
            it.copy(
                status = FavoriteUIState.FavoriteStatus.LOADING,
                vacancyList = emptyList(),
            )
        }

        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            when (val result = loadVacancies()) {
                is VacancyResult.Success -> handleSuccess(result)
                is VacancyResult.Error -> handleError(result.exception)
            }
        }
    }

    private suspend fun loadVacancies(): VacancyResult {
        return try {
            val vacancies = repository.getAll().first()
            VacancyResult.Success(vacancies.data)
        } catch (e: IOException) {
            VacancyResult.Error(e)
        } catch (e: CancellationException) {
            throw e // Пробрасываем корутиновые отмены
        }
    }

    private fun handleSuccess(result: VacancyResult.Success) {
        val vacancies = result.result?.vacancies ?: emptyList()
        val totalFound = result.result?.found ?: 0
        _state.update { current ->
            current.copy(
                status = when {
                    vacancies.isEmpty() -> FavoriteUIState.FavoriteStatus.EMPTY_RESULT
                    else -> FavoriteUIState.FavoriteStatus.SUCCESS
                },
                vacancyList = vacancies,
                totalFound = totalFound,
            )
        }
    }

    private fun handleError(exception: Exception) {
        _state.update {
            it.copy(status = FavoriteUIState.FavoriteStatus.ERROR)
        }
    }

    private sealed class VacancyResult {
        data class Success(val result: VacancySearchResult?) : VacancyResult()
        data class Error(val exception: Exception) : VacancyResult()
    }
}
