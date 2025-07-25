package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
    override fun search(query: String, page: Int): Flow<List<Vacancy>?> {
        return repository.search(query)
    }
}
