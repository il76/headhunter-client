package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.domain.models.VacancySearchResult

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
    override fun search(query: String, page: Int): Flow<Result<VacancySearchResult>> {
        return repository.search(query)
    }

    override fun getVacancyDetails(vacancyId: String): Flow<Result<VacancyDetailsState>> {
        return repository.getVacancyDetails(vacancyId)
    }
}
