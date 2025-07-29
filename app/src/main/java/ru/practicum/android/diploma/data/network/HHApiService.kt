package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface HHApiService {
    @GET("vacancies?")
    suspend fun getVacancies(
        @Query("text", encoded = false) text: String,
        @Query("page", encoded = false) page: String
    ) : VacancySearchResponse

    @GET("vacancies/{vacancyId}")
    suspend fun getVacancyDetails(@Path("vacancyId") vacancyId: String): VacancyDetailsResponse
}
