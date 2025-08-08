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

    private val stateLiveData = MutableLiveData<IndustriesState>()
    fun observeState(): LiveData<IndustriesState> = stateLiveData

    fun loadIndustries() {
        viewModelScope.launch {
            renderState(IndustriesState.Loading)

            industriesSearchInteractor.getIndustries().collect {
                val industries = mutableListOf<Industry>()
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
                                industries = industries,
                            )
                        )
                    }
                }
            }
        }
    }

    private fun renderState(state: IndustriesState) {
        stateLiveData.postValue(state)
    }
}
