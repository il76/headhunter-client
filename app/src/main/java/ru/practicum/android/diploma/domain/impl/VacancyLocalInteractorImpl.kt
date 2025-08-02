package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.VacancyLocalInteractor
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.models.VacancySearchResult

class VacancyLocalInteractorImpl(private val repository: VacancyLocalRepository) : VacancyLocalInteractor {
    override fun getAll(): Flow<Result<VacancySearchResult>> {
        return repository.getAll()
    }
}
