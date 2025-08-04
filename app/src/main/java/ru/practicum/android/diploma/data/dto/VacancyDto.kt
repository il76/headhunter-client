package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class VacancyDto(
    val id: String,
    val name: String,
    val area: Area?,
    val salary: Salary?,
    val employer: Employer?,
    @SerializedName("published_at")
    val publishedAt: String,
    @SerializedName("alternate_url")
    val alternateUrl: String,
    val schedule: Schedule?,
)
