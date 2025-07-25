package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface VacancyRepository {
    fun searchVacancies(expression: String): Flow<Resource<List<Vacancy>>>

}
