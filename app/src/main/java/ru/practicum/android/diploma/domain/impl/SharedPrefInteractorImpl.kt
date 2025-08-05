package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.api.SharedPrefInteractor
import ru.practicum.android.diploma.domain.api.SharedPrefRepository
import ru.practicum.android.diploma.domain.models.Filter

class SharedPrefInteractorImpl(private val sharedPrefsRepository: SharedPrefRepository) : SharedPrefInteractor {
    override fun getFilter(): Filter {
        return sharedPrefsRepository.getFilter()
    }

    override fun updateFilter(updatedFilter: Filter) {
        sharedPrefsRepository.updateFilter(updatedFilter)
    }

    override fun clearFilterField(field: String) {
        sharedPrefsRepository.clearFilterField(field)
    }

    override fun clearFilter() {
        sharedPrefsRepository.clearFilter()
    }
}
