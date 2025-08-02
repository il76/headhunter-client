package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.domain.models.VacancySearchResult

interface VacancyInteractor {
    fun search(query: String, page: Int = 0): Flow<Result<VacancySearchResult>>

    fun getVacancyDetails(vacancyId: String): Flow<Result<VacancyDetailsState>>
}
