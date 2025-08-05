package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Filter

interface SharedPrefRepository {
    fun getFilter(): Filter
    fun updateFilter(updatedFilter: Filter)
    fun clearFilterField(field: String)
    fun clearFilter()
}
