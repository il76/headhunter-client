package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface HHApiService {
    @Headers("Authorization: Bearer HAFLB5UH6S9C50GVR929FDIIAU05R0Q8B6RF2880N1T19BMVGH0KMLBL3R3O2J4K",
        "Content-Type: application/json")
//        "HH-User-Agent: App")
    @GET("vacancies?")
    suspend fun getVacancies(@Query("text", encoded = false) text: String): VacancySearchResponse
}
