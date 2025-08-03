package ru.practicum.android.diploma.data.dto

data class VacancyDetailsResponse(
    val id: String,
    val name: String,
    val area: Area?,
    val salary: Salary?,
    val employer: Employer?,
    val experience: Experience?,
    val employment: Employment?,
    val description: String?,
    val keySkills: List<KeySkill>?,
    val alternateUrl: String?
) : Response()
