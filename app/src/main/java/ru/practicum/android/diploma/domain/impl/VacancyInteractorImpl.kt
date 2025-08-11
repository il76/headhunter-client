package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

class VacancyInteractorImpl(private val repository: VacancyRepository) : VacancyInteractor {
    override fun search(
        query: String,
        page: Int,
        onlyWithSalary: Boolean,
        area: String?,
        industry: String?,
        salary: Long?
    ): Flow<Resource<VacancySearchResult>> {
        return repository.search(
            query = query,
            page = page,
            onlyWithSalary = onlyWithSalary,
            area = area,
            industry = industry,
            salary = salary,
        )
    }

    override fun getVacancyDetails(vacancyId: String): Flow<VacancyDetailsState> {
        return repository.getVacancyDetails(vacancyId)
    }
}
