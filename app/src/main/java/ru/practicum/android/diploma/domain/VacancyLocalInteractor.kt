package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

interface VacancyLocalInteractor {
    fun getAll(): Flow<Resource<VacancySearchResult>>
    fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>>
}
