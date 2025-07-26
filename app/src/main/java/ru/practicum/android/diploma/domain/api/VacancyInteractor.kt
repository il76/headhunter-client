package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState

interface VacancyInteractor {
    fun search(query: String, page: Int = 0): Flow<List<Vacancy>?>

    fun getVacancyDetails(vacancyId: String): Flow<VacancyDetailsState>
}
