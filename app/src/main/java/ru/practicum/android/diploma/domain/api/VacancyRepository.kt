package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacancyRepository {
    fun search(query: String, page: Int = 0): Flow<List<Vacancy>?>
}
