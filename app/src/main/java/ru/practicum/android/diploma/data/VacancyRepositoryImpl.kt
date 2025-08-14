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
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyRepository {
    override fun search(
        query: String,
        page: Int,
        onlyWithSalary: Boolean,
        area: String?,
        industry: String?,
        salary: Long?
    ): Flow<Resource<VacancySearchResult>> = flow {
        val response = networkClient.doRequest(
            VacancySearchRequest(
                text = query,
                page = page,
                onlyWithSalary = onlyWithSalary,
                area = area,
                industry = industry,
                salary = salary,
            )
        )
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
                                    logoUrl = it.employer?.logoUrls?.original ?: "",
                                    areaName = it.area?.name ?: "",
                                    employerName = it.employer?.name ?: "",
                                    salaryCurrency = it.salary?.currency ?: "",
                                    salaryFrom = it.salary?.from,
                                    salaryTo = it.salary?.to,
                                    schedule = it.schedule?.name,
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
                val vacancy = createVacancyFromResponse(response as VacancyDetailsResponse)
                emit(VacancyDetailsState.ContentState(vacancy))
            }
            else -> emit(VacancyDetailsState.NetworkErrorState(response.resultError))
        }
    }

    private fun createVacancyFromResponse(response: VacancyDetailsResponse): Vacancy {
        return Vacancy(
            id = response.id,
            name = response.name,
            logoUrl = response.employer?.logoUrls?.original ?: "",
            areaName = response.area?.name ?: "",
            employment = response.employer?.name ?: "",
            salaryTo = response.salary?.to,
            salaryFrom = response.salary?.from,
            experience = response.experience?.name ?: "",
            description = response.description ?: "",
            keySkills = response.keySkills?.map { it.name },
            employerName = response.employer?.name ?: "",
            salaryCurrency = response.salary?.currency ?: "",
            schedule = response.schedule?.name,
            contacts = response.contacts
        )
    }

    companion object {
        const val REQUEST_OK = 200
        const val NO_INTERNET = 0
    }
}
