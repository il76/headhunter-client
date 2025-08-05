package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.VacancyLocalInteractor
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

class VacancyLocalInteractorImpl(private val repository: VacancyLocalRepository) : VacancyLocalInteractor {
    override suspend fun getAll(): Flow<Resource<VacancySearchResult>> {
        return repository.getAll()
    }

    override suspend fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>> {
        return repository.getVacancyDetails(vacancyId)
    }

    override suspend fun saveVacancy(vacancy: Vacancy) {
        repository.saveVacancy(vacancy)
    }

    override suspend fun deleteVacancy(vacancyId: String) {
        repository.deleteVacancy(vacancyId)
    }
}
