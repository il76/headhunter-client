package ru.practicum.android.diploma.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.IndustriesState
import ru.practicum.android.diploma.domain.IndustriesSearchInteractor
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesViewModel(
    private val industriesSearchInteractor: IndustriesSearchInteractor
) : ViewModel() {

    var selectedIndustry: Industry? = null
    var filterText: String = ""
    private val industries = mutableListOf<Industry>()
    private val stateLiveData = MutableLiveData<IndustriesState>()
    fun observeState(): LiveData<IndustriesState> = stateLiveData

    fun loadIndustries(selectedIndustry: Industry?) {
        viewModelScope.launch {
            renderState(IndustriesState.Loading)

            industriesSearchInteractor.getIndustries().collect {
                industries.clear()
                if (it.first != null) {
                    industries.addAll(it.first!!)
                }

                when {
                    it.second != null -> {
                        renderState(
                            IndustriesState.Error(
                                errorMessageId = R.string.search_no_internet,
                            )
                        )
                    }
                    else -> {
                        renderState(
                            IndustriesState.Content(
                                industries = industries.map { indust ->
                                    Industry(
                                        id = indust.id,
                                        name = indust.name,
                                        selected = indust.id == selectedIndustry?.id
                                    )
                                },
                            )
                        )
                        industryClicked(selectedIndustry)
                    }
                }
            }
        }
    }

    fun industryClicked(industry: Industry?) {
        if (selectedIndustry?.id == industry?.id) {
            selectedIndustry = null
        } else {
            selectedIndustry = industry
        }
        renderState(
            IndustriesState.Content(
                industries = filterIndustries(filterText),
            )
        )
    }

    fun hasSelectedIndustries(): Boolean =
        selectedIndustry != null

    fun filterIndustries(value: String): List<Industry> {
        filterText = value
        val res = mutableListOf<Industry>()
        if (value.isEmpty()) {
            res.addAll(industries)
        } else {
            industries.forEach {
                if (it.name.contains(value, true)) {
                    res.add(it)
                }
            }
        }
        return res.map { indust ->
            Industry(
                id = indust.id,
                name = indust.name,
                selected = indust.id == selectedIndustry?.id
            )
        }
    }
    private fun renderState(state: IndustriesState) {
        stateLiveData.postValue(state)
    }
}
