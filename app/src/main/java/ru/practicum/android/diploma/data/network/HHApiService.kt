package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface HHApiService {
    @GET("vacancies?")
    suspend fun getVacancies(@Query("text", encoded = false) text: String): VacancySearchResponse
}
