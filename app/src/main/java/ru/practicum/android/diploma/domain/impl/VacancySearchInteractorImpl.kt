package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.VacancyRepository
import ru.practicum.android.diploma.domain.VacancySearchInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class VacancySearchInteractorImpl(
    private val repository: VacancyRepository
) : VacancySearchInteractor {
    override fun searchVacancies(expression: String): Flow<Pair<List<Vacancy>?, String?>> {
        return repository.searchVacancies(expression).map {result ->
            when(result) {
                is Resource.Success -> {Pair(result.data, null)}
                is Resource.Error -> {Pair(null, result.message)}
            }
        }
    }
}
