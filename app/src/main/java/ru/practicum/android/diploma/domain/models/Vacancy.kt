package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val logoUrl: String,
    val areaName: String,
    val employerName: String,
    val salaryCurrency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val experience: String? = null,
    val employment: String? = null,
    val description: String? = null,
    val keySkills: List<String>? = null,
    val schedule: String?,
)
