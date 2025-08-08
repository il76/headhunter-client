package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.api.SharedPrefRepository
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.util.APP_SHARED_PREFS
import ru.practicum.android.diploma.util.FILTER_INDUSTRY
import ru.practicum.android.diploma.util.FILTER_ONLY_SALARY
import ru.practicum.android.diploma.util.FILTER_SALARY

class SharedPrefRepositoryImpl(private val sharedPreferences: SharedPreferences, private val gson: Gson) :
    SharedPrefRepository {

    override fun getFilter(): Filter {
        val jsonString = sharedPreferences.getString(SHARED_FILTER_KEY, null)
        return if (jsonString != null) {
            gson.fromJson(jsonString, Filter::class.java)
        } else {
            Filter(industry = null, salary = null, onlyWithSalary = null)
        }
    }

    override fun updateFilter(updatedFilter: Filter) {
        val currentFilter = getFilter()

        val mergedFilter = Filter(
            industry = updatedFilter.industry ?: currentFilter.industry,
            salary = updatedFilter.salary ?: currentFilter.salary,
            onlyWithSalary = if (updatedFilter.onlyWithSalary == false) {
                currentFilter.onlyWithSalary
            } else {
                updatedFilter.onlyWithSalary
            }
        )
        println(updatedFilter)
        saveFilter(mergedFilter)
    }

    override fun clearFilterField(field: String) {
        val currentFilter = getFilter()

        val updatedFilter = when (field) {
            FILTER_INDUSTRY -> currentFilter.copy(industry = null)
            FILTER_SALARY -> currentFilter.copy(salary = null)
            FILTER_ONLY_SALARY -> currentFilter.copy(onlyWithSalary = false)
            else -> currentFilter
        }
        saveFilter(updatedFilter)
    }

    override fun clearFilter() {
        sharedPreferences.edit { clear() }
    }

    fun saveFilter(filter: Filter) {
        val jsonString = gson.toJson(filter)
        sharedPreferences.edit { putString(SHARED_FILTER_KEY, jsonString) }
    }

    companion object {
        const val SHARED_FILTER_KEY = APP_SHARED_PREFS
    }
}
