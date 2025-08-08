package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.SharedPrefInteractor
import ru.practicum.android.diploma.domain.models.Filter

class FilterViewModel(private val sharedPrefInteractor: SharedPrefInteractor) : ViewModel() {
    private val _currentFilter = MutableLiveData<Filter>()
    val currentFilter: LiveData<Filter> get() = _currentFilter

    private val _updatedFilter = MutableLiveData<Filter>()
    val updatedFilter: LiveData<Filter> get() = _updatedFilter

    init {
        _currentFilter.value = sharedPrefInteractor.getFilter()
    }

    fun updateFilter(filter: Filter) {
        sharedPrefInteractor.updateFilter(filter)
        refreshUpdatedFilter()
    }

    fun clearFilterField(field: String) {
        sharedPrefInteractor.clearFilterField(field)
        refreshUpdatedFilter()
    }

    fun clearFilter() {
        sharedPrefInteractor.clearFilter()
        refreshUpdatedFilter()
    }

    fun refreshUpdatedFilter() {
        val updatedFilter = sharedPrefInteractor.getFilter()
        _updatedFilter.value = updatedFilter
    }
}
