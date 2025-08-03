package ru.practicum.android.diploma.ui.favorite

import ru.practicum.android.diploma.domain.models.Vacancy

data class FavoriteUIState(
    val vacancyList: List<Vacancy> = emptyList(), // Список найденного
    val totalFound: Int = 0,
    val status: FavoriteStatus = FavoriteStatus.NONE, // Состояние результатов поиска

) {
    enum class FavoriteStatus { NONE, LOADING, ERROR, EMPTY_RESULT, SUCCESS }
}
