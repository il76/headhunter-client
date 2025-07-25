package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyRepository {
    override fun search(query: String, page: Int): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(query, 1))
        when (response.resultCode) {
            NO_INTERNET -> emit(Resource.Error("Check connection to internet"))
            REQUEST_OK -> {
                emit(
                    Resource.Success(
                        (response as VacancySearchResponse).items.map {
                            Vacancy(
                                id = it.id,
                                name = it.name,
                                logoUrl = it.employer?.logoUrls?.original.toString(),
                                areaName = it.area?.name?:"",
                                employerName = it.employer?.name?:"",
                                salaryCurrency = it.salary?.currency?:"",
                                salaryFrom = it.salary?.from,
                                salaryTo = it.salary?.to,
                            )
                        }
                    )
                )
            }
            else -> emit(Resource.Error(response.resultError))
        }

    }
    companion object {
        const val REQUEST_OK = 200
        const val NO_INTERNET = -1
    }
}
