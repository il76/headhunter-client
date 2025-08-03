package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.VacancyLocalInteractor
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

class VacancyLocalInteractorImpl(private val repository: VacancyLocalRepository) : VacancyLocalInteractor {
    override fun getAll(): Flow<Resource<VacancySearchResult>> {
        return repository.getAll()
    }

    override fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>> {
        return repository.getVacancyDetails(vacancyId)
    }
}
