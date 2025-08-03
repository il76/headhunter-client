package ru.practicum.android.diploma.domain.models

data class VacancyFull(
    val id: Int,
    val name: String,
    val company: String = "",
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val currency: String = "",
    val area: String,
    val alternateUrl: String,
    val icon: String? = "",
    val schedule: String = "",
    val employment: String = "",
    val experience: String,
    val description: String,
    val keySkills: String,
    val address: String = ""
)
