package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import kotlin.String

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyRepository {
    override fun search(query: String, page: Int): Flow<Result<VacancySearchResult>> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(query, page))
        when (response.resultCode) {
            NO_INTERNET -> emit(Result.failure(Exception("Check connection to internet")))
            REQUEST_OK -> {
                val searchResponse = response as VacancySearchResponse
                val result = VacancySearchResult(
                    vacancies = searchResponse.items.map {
                        Vacancy(
                            id = it.id,
                            name = it.name,
                            logoUrl = it.employer?.logoUrls?.original.orEmpty(),
                            areaName = it.area?.name.orEmpty(),
                            employerName = it.employer?.name.orEmpty(),
                            salaryCurrency = it.salary?.currency.orEmpty(),
                            salaryFrom = it.salary?.from,
                            salaryTo = it.salary?.to,
                        )
                    },
                    found = searchResponse.found
                )
                emit(Result.success(result))
            }
            else -> emit(Result.failure(Exception(response.resultError)))
        }
    }

    override fun getVacancyDetails(vacancyId: String): Flow<Result<VacancyDetailsState>> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
        when (response.resultCode) {
            NO_INTERNET -> emit(Result.failure(Exception("No internet connection")))
            REQUEST_OK -> {
                val details = with(response as VacancyDetailsResponse) {
                    VacancyFull(
                        id = this.id.toInt(),
                        name = this.name,
                        icon = this.employer?.logoUrls?.original.orEmpty(),
                        area = this.area?.name.orEmpty(),
                        employment = this.employer?.name.orEmpty(),
                        salaryTo = this.salary?.to,
                        salaryFrom = this.salary?.from,
                        experience = this.experience?.name.orEmpty(),
                        description = this.description.orEmpty(),
                        keySkills = this.keySkills?.map { it.name }.orEmpty().toString(),
                        alternateUrl = this.alternateUrl.orEmpty(),
                        currency = this.salary?.currency.orEmpty()
                    )
                }
                emit(Result.success(VacancyDetailsState.ContentState(details)))
            }
            else -> emit(Result.failure(Exception(response.resultError)))
        }
    }

    companion object {
        const val REQUEST_OK = 200
        const val NO_INTERNET = -1
    }
}
