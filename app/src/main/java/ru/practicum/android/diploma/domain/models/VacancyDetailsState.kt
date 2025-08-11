package ru.practicum.android.diploma.domain.models

sealed interface VacancyDetailsState {
    data object LoadingState : VacancyDetailsState
    data class ContentState(val vacancy: Vacancy) : VacancyDetailsState
    data class EmptyState(val message: String) : VacancyDetailsState
    data class NetworkErrorState(val message: String) : VacancyDetailsState
    data object ServerError : VacancyDetailsState
    data object ConnectionError : VacancyDetailsState
}
