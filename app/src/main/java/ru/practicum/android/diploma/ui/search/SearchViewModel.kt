package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class SearchViewModel(private val repository: VacancyRepository) : ViewModel() {

    var vacancyList: List<Vacancy> = mutableListOf()

    private val _state = MutableStateFlow<SearchUIState>(SearchUIState())
    val state: StateFlow<SearchUIState> = _state.asStateFlow()

    private var currentPage = 0

    private var searchJob: Job? = null

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
            _state.value.pagination is SearchUIState.PaginationState.LOADING
        ) {
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
            loadVacancies(page)
                .onSuccess { handleSuccess(it, page, isInitial) }
                .onFailure { handleError(it as Exception, isInitial) }
        }
    }

    private suspend fun loadVacancies(page: Int): Result<VacancySearchResult> {
        return try {
            repository.search(_state.value.searchQuery, page).first()
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: IllegalStateException) {
            Result.failure(e)
        }
    }

    private fun handleSuccess(result: VacancySearchResult, page: Int, isInitial: Boolean) {
        val vacancies = result.vacancies
        val totalFound = result.found
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
                totalFound = totalFound,
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

    fun updateSearchQuery(newQuery: String) {
        _state.update { currentState ->
            currentState.copy(searchQuery = newQuery)
        }
        // Отменяем предыдущий запрос, если он есть
        searchJob?.cancel()

        // Если запрос не пустой, запускаем новый отложенный поиск
        if (newQuery.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                search(newQuery)
            }
        } else {
            // Если запрос пустой, очищаем результаты
            _state.update {
                it.copy(
                    status = SearchUIState.SearchStatus.NONE,
                    vacancyList = emptyList()
                )
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}
