package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.IndustriesSearchInteractor
import ru.practicum.android.diploma.domain.api.IndustriesRepository
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.util.Resource

class IndustriesSearchInteractorImpl(
    private val repository: IndustriesRepository
) : IndustriesSearchInteractor {
    override fun getIndustries(): Flow<Pair<List<Industry>?, String?>> {
        return repository.getIndustries().map { result ->
            when (result) {
                is Resource.Success -> { Pair(result.data?.industries, null) }
                is Resource.Error -> { Pair(null, result.message) }
            }
        }
    }
}
