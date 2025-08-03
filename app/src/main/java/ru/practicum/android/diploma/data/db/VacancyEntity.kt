package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies", indices = [Index(value = ["id"], unique = true)])
data class VacancyEntity(
    @PrimaryKey()
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
)
