package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

interface VacancyInteractor {
    fun search(
        query: String,
        page: Int = 0,
        onlyWithSalary: Boolean = false,
        area: String?,
        industry: String?,
        salary: Long?
    ): Flow<Resource<VacancySearchResult>>

    fun getVacancyDetails(vacancyId: String): Flow<VacancyDetailsState>
}
