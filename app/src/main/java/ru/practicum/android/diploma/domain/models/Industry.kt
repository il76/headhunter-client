package ru.practicum.android.diploma.domain.models

import android.os.Parcel
import android.os.Parcelable

data class Industry(
    val id: String,
    val name: String,
    val selected: Boolean = false,
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }
}
