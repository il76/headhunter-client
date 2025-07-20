package ru.practicum.android.diploma.data.dto

data class VacancyDto(
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?,
    val employment: Employment,
    val description: String,
    val experience: Experience,
    val key_skills: List<KeySkill>,
)
