package ru.practicum.android.diploma.domain.models

data class VacancySearchResult(
    val vacancies: List<Vacancy>,
    val found: Int
)
