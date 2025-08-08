package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.IndustryDto
import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.VacancySearchResponse

interface HHApiService {
    @GET("vacancies")
    suspend fun getVacancies(
        @Query("text") text: String,
        @Query("page") page: String = "1",
        @Query("area") area: String? = null,
        @Query("industry") industry: String? = null,
        @Query("salary") salary: Long? = null,
        @Query("only_with_salary") onlyWithSalary: Boolean = false,
    ): VacancySearchResponse

    @GET("vacancies/{vacancyId}")
    suspend fun getVacancyDetails(@Path("vacancyId") vacancyId: String): VacancyDetailsResponse

    @GET("industries")
    suspend fun getIndustries(): List<IndustryDto>
}
