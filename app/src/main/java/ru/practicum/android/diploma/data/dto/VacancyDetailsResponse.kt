package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDetailsResponse(
    val id: String,
    val name: String,
    val area: Area?,
    val address: Address?,
    val salary: Salary?,
    val employer: Employer?,
    val experience: Experience?,
    val employment: Employment?,
    val description: String?,
    @SerializedName("key_skills")
    val keySkills: List<KeySkill>?,
    val schedule: Schedule?,
    val contacts: Contacts?,
) : Response()
