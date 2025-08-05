package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.IndustrySearchResult

interface FilterRepository {
    fun getIndustries(): Flow<IndustrySearchResult>
}
