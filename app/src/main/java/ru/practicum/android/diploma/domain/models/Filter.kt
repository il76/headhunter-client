package ru.practicum.android.diploma.domain.models

data class Filter(
    val salary: Int? = null,
    val industry: Industry? = null,
    val onlyWithSalary: Boolean? = null
)
