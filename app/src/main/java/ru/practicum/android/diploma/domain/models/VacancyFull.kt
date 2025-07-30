package ru.practicum.android.diploma.domain.models

data class VacancyFull(
    val id: Int = -1,
    val name: String = "",
    val company: String = "",
    val currency: String = "",
    val salaryFrom: Int? = null,
    val salaryTo: Int? = null,
    val area: String = "",
    val alternateUrl: String = "",
    val icon: String = "",
    val employment: String = "",
    val experience: String = "",
    val schedule: String = "",
    val description: String = "",
    val contact: String = "",
    val email: String = "",
    val phone: String = "",
    val comment: String = "",
    val keySkills: String = "",
    val address: String = ""
)
