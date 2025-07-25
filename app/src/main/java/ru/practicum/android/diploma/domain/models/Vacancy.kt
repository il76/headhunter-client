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
    val experience: String,
    val employment: String,
    val description: String,
    val keySkills: List<String>,
)
