package ru.practicum.android.diploma.domain.api

import ru.practicum.android.diploma.domain.models.Filter

interface SharedPrefInteractor {
    fun getFilter(): Filter
    fun updateFilter(updatedFilter: Filter)
    fun clearFilterField(field: String)
    fun clearFilter()
}
