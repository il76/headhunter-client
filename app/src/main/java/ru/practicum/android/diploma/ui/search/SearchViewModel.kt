package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class SearchViewModel(private val repository: VacancyRepository) : ViewModel() {

    var vacancyList: List<Vacancy> = mutableListOf()

    private val _state = MutableStateFlow<SearchUIState>(SearchUIState())
    val state: StateFlow<SearchUIState> = _state.asStateFlow()

    private var currentPage = 0

    fun search(query: String) {
        currentPage = 0
        _state.update {
            it.copy(
                searchQuery = query,
                status = SearchUIState.SearchStatus.LOADING,
                vacancyList = emptyList(),
                canLoadMore = true,
                isRefreshing = false
            )
        }

        loadData(page = 0, isInitial = true)
    }

    fun refresh() {
        if (_state.value.isRefreshing) return

        currentPage = 0
        _state.update {
            it.copy(
                isRefreshing = true,
                canLoadMore = true
            )
        }

        loadData(page = 0, isInitial = false)
    }

    // Подгрузка следующей страницы (при скролле)
    fun loadNextPage() {
        if (!_state.value.canLoadMore ||
            _state.value.pagination is SearchUIState.PaginationState.LOADING) {
            return
        }

        currentPage++
        _state.update { it.copy(pagination = SearchUIState.PaginationState.LOADING) }

        loadData(page = currentPage, isInitial = false)
    }

    // Повторная попытка при ошибке пагинации
    fun retryPagination() {
        if (_state.value.pagination is SearchUIState.PaginationState.ERROR) {
            loadNextPage()
        }
    }

    private fun loadData(page: Int, isInitial: Boolean) {
        viewModelScope.launch {
            when (val result = loadVacancies(page)) {
                is VacancyResult.Success -> handleSuccess(result, page, isInitial)
                is VacancyResult.Error -> handleError(result.exception, isInitial)
            }
        }
    }

    private suspend fun loadVacancies(page: Int): VacancyResult {
        return try {
            val vacancies = repository.search(_state.value.searchQuery, page).first()
            VacancyResult.Success(vacancies)
        } catch (e: IOException) {
            VacancyResult.Error(e)
        } catch (e: CancellationException) {
            throw e // Пробрасываем корутиновые отмены
// опять detekt!
//        } catch (e: Exception) {
//            VacancyResult.Error(IOException("Network error", e))
        }
    }

    private fun handleSuccess(result: VacancyResult.Success, page: Int, isInitial: Boolean) {
        val vacancies = result.vacancies ?: emptyList()
        val canLoadMore = vacancies.size == PAGE_SIZE

        _state.update { current ->
            val newVacancies = if (page == 0) vacancies else current.vacancyList + vacancies

            current.copy(
                status = when {
                    !isInitial -> current.status
                    newVacancies.isEmpty() -> SearchUIState.SearchStatus.EMPTY_RESULT
                    else -> SearchUIState.SearchStatus.SUCCESS
                },
                vacancyList = newVacancies,
                pagination = SearchUIState.PaginationState.IDLE,
                canLoadMore = canLoadMore,
                isRefreshing = false
            )
        }
    }

    private fun handleError(exception: Exception, isInitial: Boolean) {
        _state.update {
            if (isInitial) {
                it.copy(status = SearchUIState.SearchStatus.ERROR_NET)
            } else {
                it.copy(
                    pagination = SearchUIState.PaginationState.ERROR(exception),
                    isRefreshing = false
                )
            }
        }
    }

    private sealed class VacancyResult {
        data class Success(val vacancies: List<Vacancy>?) : VacancyResult()
        data class Error(val exception: Exception) : VacancyResult()
    }

    companion object {
        const val PAGE_SIZE = 20
    }

}
