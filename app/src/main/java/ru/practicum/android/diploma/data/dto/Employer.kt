package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class Employer(
    @SerializedName("logo_urls")
    val logoUrls: LogoUrls,
    val name: String,
)
