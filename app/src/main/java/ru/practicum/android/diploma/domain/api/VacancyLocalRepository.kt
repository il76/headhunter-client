package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

interface VacancyLocalRepository {
    fun getAll(): Flow<Resource<VacancySearchResult>>
}
