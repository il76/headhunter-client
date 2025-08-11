package ru.practicum.android.diploma.data

import androidx.annotation.StringRes
import ru.practicum.android.diploma.domain.models.Industry

sealed interface IndustriesState {
    object Loading : IndustriesState

    data class Content(
        val industries: List<Industry>
    ) : IndustriesState

    data class Error(
        @StringRes val errorMessageId: Int
    ) : IndustriesState
}
