package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.util.Resource

interface VacancyRepository {
    fun search(query: String, page: Int = 0): Flow<Resource<List<Vacancy>>>

    fun getVacancyDetails(vacancyId: String): Flow<VacancyDetailsState>
}
