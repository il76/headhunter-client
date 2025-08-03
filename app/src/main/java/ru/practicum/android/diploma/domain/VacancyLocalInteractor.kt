package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancySearchResult

interface VacancyLocalInteractor {
    fun getAll(): Flow<Result<VacancySearchResult>>
}
