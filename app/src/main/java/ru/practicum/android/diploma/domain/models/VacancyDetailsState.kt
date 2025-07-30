package ru.practicum.android.diploma.domain.models

sealed interface VacancyDetailsState {
    data object LoadingState : VacancyDetailsState
    data class ContentState(val vacancy: VacancyFull) : VacancyDetailsState
    data object EmptyState : VacancyDetailsState
    data class NetworkErrorState(val message: String) : VacancyDetailsState
    data object ServerError : VacancyDetailsState
    data object ConnectionError : VacancyDetailsState
}
