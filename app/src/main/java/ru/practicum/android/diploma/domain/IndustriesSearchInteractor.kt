package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Industry

interface IndustriesSearchInteractor {
    fun getIndustries(): Flow<Pair<List<Industry>?, String?>>
}
