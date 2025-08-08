package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.IndustrySearchResult
import ru.practicum.android.diploma.util.Resource

interface IndustriesRepository {
    fun getIndustries(): Flow<Resource<IndustrySearchResult>>
}
