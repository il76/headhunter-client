package ru.practicum.android.diploma.data.dto

data class VacancySearchRequest(
    val text: String,
    val page: Int,
    val onlyWithSalary: Boolean = false,
    val area: String? = null,
    val industry: String? = null,
    val salary: Long? = null,
)
