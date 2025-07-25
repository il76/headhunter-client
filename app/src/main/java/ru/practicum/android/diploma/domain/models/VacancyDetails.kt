package ru.practicum.android.diploma.domain.models

data class VacancyDetails(
    val id: String,
    val experience: String,
    val employment: String,
    val description: String,
    val keySkills: List<String>,
)
