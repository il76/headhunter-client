package ru.practicum.android.diploma.ui.search

import ru.practicum.android.diploma.domain.models.Vacancy

data class SearchUIState(
    val searchQuery: String = "", // Поисковый запрос
    val vacancyList: List<Vacancy> = emptyList(), // Список найденного
    val totalFound: Int = 0,
    val status: SearchStatus = SearchStatus.NONE, // Состояние результатов поиска
    val pagination: PaginationState = PaginationState.IDLE, // Состояние пагинации
    val canLoadMore: Boolean = true, // Можно ли загружать ещё
    val isRefreshing: Boolean = false, // Для Pull-to-Refresh
) {
    enum class SearchStatus { NONE, LOADING, ERROR_NET, EMPTY_RESULT, SUCCESS }

    sealed class PaginationState {
        data object IDLE : PaginationState()
        data object LOADING : PaginationState()
        data class ERROR(val error: Throwable) : PaginationState()
    }
}
