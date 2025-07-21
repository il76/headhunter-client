package ru.practicum.android.diploma.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies", indices = [Index(value = ["id"], unique = true)])
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
)
