package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacancySearchInteractor {
    fun searchVacancies(expression: String): Flow<Pair<List<Vacancy>?, String?>>

}
