package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancySearchResult

interface VacancyLocalRepository {
    fun getAll(): Flow<Result<VacancySearchResult>>
}
