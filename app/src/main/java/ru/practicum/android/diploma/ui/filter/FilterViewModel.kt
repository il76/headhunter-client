package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.models.Filter

class FilterViewModel : ViewModel() {
    private val _currentFilter = MutableLiveData<Filter>()
    val currentFilter: LiveData<Filter> get() = _currentFilter

    private val _updatedFilter = MutableLiveData<Filter>()
    val updatedFilter: LiveData<Filter> get() = _updatedFilter

    init {
        _currentFilter.value = Filter()
    }

    fun updateFilter(filter: Filter) {
        _updatedFilter.value = filter
    }

    fun clearFilterField(field: String) {
        val currentFilter = _updatedFilter.value

        _updatedFilter.value = when (field) {
            "industry" -> currentFilter?.copy(industry = null)
            "salary" -> currentFilter?.copy(salary = null)
            "onlyWithSalary" -> currentFilter?.copy(onlyWithSalary = false)
            else -> currentFilter
        }
    }

    fun clearFilter() {
        refreshUpdatedFilter()
    }

    fun refreshUpdatedFilter() {
        val updatedFilter = Filter()
        _updatedFilter.value = updatedFilter
    }
}

