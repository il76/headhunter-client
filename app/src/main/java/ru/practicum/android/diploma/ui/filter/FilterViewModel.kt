package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.api.SharedPrefInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.Industry

class FilterViewModel(private val sharedPrefInteractor: SharedPrefInteractor) : ViewModel() {
    private val _currentFilter = MutableLiveData<Filter>()
    val currentFilter: LiveData<Filter> get() = _currentFilter

    init {
        _currentFilter.value = sharedPrefInteractor.getFilter()
    }

    fun updateFilter(
        salary: Int? = _currentFilter.value?.salary,
        industry: Industry? = _currentFilter.value?.industry,
        onlyWithSalary: Boolean? = _currentFilter.value?.onlyWithSalary
    ) {
        val newFilter = Filter(
            salary = salary,
            industry = industry,
            onlyWithSalary = onlyWithSalary
        )
        sharedPrefInteractor.updateFilter(newFilter)
        _currentFilter.value = newFilter
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
        _currentFilter.value = updatedFilter
    }
}
