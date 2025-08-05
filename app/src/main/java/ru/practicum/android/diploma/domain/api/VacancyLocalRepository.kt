package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

interface VacancyLocalRepository {
    suspend fun getAll(): Flow<Resource<VacancySearchResult>>
    suspend fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>>
    suspend fun saveVacancy(vacancy: Vacancy)
    suspend fun deleteVacancy(vacancyId: String)

}
