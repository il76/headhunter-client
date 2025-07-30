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
import ru.practicum.android.diploma.util.Resource
import kotlin.String

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyRepository {
    override fun search(query: String, page: Int): Flow<Resource<VacancySearchResult>> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(query, page))
        when (response.resultCode) {
            NO_INTERNET -> emit(Resource.Error("Check connection to internet"))
            REQUEST_OK -> {
                val searchResponse = response as VacancySearchResponse
                emit(
                    Resource.Success(
                        VacancySearchResult(
                            vacancies = searchResponse.items.map {
                                Vacancy(
                                    id = it.id,
                                    name = it.name,
                                    logoUrl = it.employer?.logoUrls?.original.toString(),
                                    areaName = it.area?.name ?: "",
                                    employerName = it.employer?.name ?: "",
                                    salaryCurrency = it.salary?.currency ?: "",
                                    salaryFrom = it.salary?.from,
                                    salaryTo = it.salary?.to,
                                )
                            },
                            found = searchResponse.found
                        )
                    )
                )
            }
            else -> emit(Resource.Error(response.resultError))
        }
    }

    override fun getVacancyDetails(vacancyId: String): Flow<VacancyDetailsState> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
        when (response.resultCode) {
            NO_INTERNET -> emit(VacancyDetailsState.ConnectionError)
            REQUEST_OK -> {
                emit(VacancyDetailsState.ContentState(
                    with(response as VacancyDetailsResponse) {
                        VacancyFull(
                            id = this.id.toInt(),
                            name = this.name,
                            icon = this.employer?.logoUrls?.original.toString(),
                            area = this.area?.name ?: "",
                            employment = this.employer?.name ?: "",
                            salaryTo = this.salary?.to,
                            salaryFrom = this.salary?.from,
                            experience = this.experience?.name ?: "",
                            description = this.description ?: "",
                            keySkills = this.keySkills?.map { keySkill ->
                                keySkill.name
                            }.toString(),
                        )
                    }
                ))
            }
            else -> emit(VacancyDetailsState.NetworkErrorState(response.resultError))
        }
    }

    companion object {
        const val REQUEST_OK = 200
        const val NO_INTERNET = -1
    }
}
