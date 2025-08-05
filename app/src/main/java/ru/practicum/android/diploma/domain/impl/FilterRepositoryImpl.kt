package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.IndustrySearchResult

class FilterRepositoryImpl(
    private val filterRepository: FilterRepository,
) : FilterRepository {
    override fun getIndustries(): Flow<IndustrySearchResult> {
        return filterRepository.getIndustries()
    }
}
