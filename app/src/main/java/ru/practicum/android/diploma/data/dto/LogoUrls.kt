package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName

data class LogoUrls(
    @SerializedName("_90")
    val size90: String?,
    @SerializedName("_240")
    val size240: String?,
    val original: String?
)
