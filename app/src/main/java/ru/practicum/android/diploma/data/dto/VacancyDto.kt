package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    val name: String,
    val area: Area,
    val employer: Employer,
    val salary: Salary?,
    val employment: Employment,
    val description: String,
    val experience: Experience,
    @SerializedName("key_skills")
    val keySkills: List<KeySkill>,
)
