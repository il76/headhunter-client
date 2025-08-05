package ru.practicum.android.diploma.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson

import ru.practicum.android.diploma.domain.api.SharedPrefRepository
import ru.practicum.android.diploma.domain.models.Filter

class SharedPrefRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SharedPrefRepository {
    private val gson = Gson()

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
            "industry" -> currentFilter.copy(industry = null)
            "salary" -> currentFilter.copy(salary = null)
            "onlyWithSalary" -> currentFilter.copy(onlyWithSalary = false)
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
        const val SHARED_FILTER_KEY = "hh_preferences"
    }
}
