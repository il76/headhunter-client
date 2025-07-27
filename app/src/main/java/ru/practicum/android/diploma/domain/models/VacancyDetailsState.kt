package ru.practicum.android.diploma.domain.models

sealed interface VacancyDetailsState {
    data object LoadingState : VacancyDetailsState
    data class ContentState(val vacancy: Vacancy) : VacancyDetailsState
    data object EmptyState : VacancyDetailsState
    data object NetworkErrorState : VacancyDetailsState
    data object ServerError : VacancyDetailsState
    data object ConnectionError : VacancyDetailsState
}
