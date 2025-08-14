package ru.practicum.android.diploma.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import ru.practicum.android.diploma.domain.api.SharedPrefInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class SearchViewModel(
    private val repository: VacancyRepository,
    private val sharedPrefInteractor: SharedPrefInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<SearchUIState>(SearchUIState())
    val state: StateFlow<SearchUIState> = _state.asStateFlow()

    private val _filterState = MutableLiveData<Boolean>()
    fun filterState(): LiveData<Boolean> = _filterState

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
            when (val result = loadVacancies(page)) {
                is VacancyResult.Success -> handleSuccess(result, page, isInitial)
                is VacancyResult.Error -> handleError(result.exception, isInitial)
            }
        }
    }

    private suspend fun loadVacancies(page: Int): VacancyResult {
        return try {
            val filter = sharedPrefInteractor.getFilter()
            val vacancies = repository.search(
                _state.value.searchQuery,
                page,
                onlyWithSalary = filter.onlyWithSalary ?: false,
                industry = filter.industry?.id,
                salary = filter.salary?.toLong(),
            ).first()
            VacancyResult.Success(vacancies.data)
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
        val vacancies = result.result?.vacancies ?: emptyList()
        val totalFound = if (page == 0) result.result?.found ?: 0 else _state.value.totalFound
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
                totalFound = totalFound, // Обновляем только при первой загрузке
                pagination = SearchUIState.PaginationState.IDLE,
                canLoadMore = canLoadMore,
                isRefreshing = false
            )
        }
    }

    private fun handleError(exception: Exception, isInitial: Boolean) {
        _state.update { current ->
            if (isInitial) {
                current.copy(
                    status = SearchUIState.SearchStatus.ERROR_NET,
                    vacancyList = emptyList(),
                    totalFound = 0, // Обнуляем только при начальной загрузке
                    isRefreshing = false
                )
            } else {
                current.copy(
                    pagination = SearchUIState.PaginationState.ERROR(exception),
                    isRefreshing = false,
                    // Сохраняем предыдущие значения
                    totalFound = current.totalFound,
                    vacancyList = current.vacancyList
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

    fun renderFilterState() {
        val filter = sharedPrefInteractor.getFilter()
        if (filter.industry == null && filter.salary == null && filter.onlyWithSalary != true) {
            _filterState.postValue(false)
        } else {
            _filterState.postValue(true)
        }
    }

    private sealed class VacancyResult {
        data class Success(val result: VacancySearchResult?) : VacancyResult()
        data class Error(val exception: Exception) : VacancyResult()
    }

    companion object {
        const val PAGE_SIZE = 20
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}
