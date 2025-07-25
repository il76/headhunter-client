package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient
) : VacancyRepository {
    override fun searchVacancies(expression: String): Flow<Resource<List<Vacancy>>> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(expression))
        when (response.resultCode) {
            NO_INTERNET -> emit(Resource.Error("Check connection to internet"))
            REQUEST_OK -> {
                println("REQUEST_OK ${(response as VacancySearchResponse).results}")
//                emit(
//                    Resource.Success(
//                        (response as VacancySearchResponse).results.map {
//
//                            Vacancy(
//                                name = it.name,
//                                logoUrl = it.employer.logoUrls.original.toString(),
//                                areaName = it.area.name,
//                                employerName = it.employer.name,
//                                salaryCurrency = it.salary?.currency?:"",
//                                salaryFrom = it.salary?.from,
//                                salaryTo = it.salary?.to,
//                                experience = it.experience.name,
//                                employment = it.employment.name,
//                                description = it.description,
//                                keySkills = it.keySkills.map { skils->
//                                    skils.name
//                                }
//                            )
//                        }
//                    )
//                )
            }
            else -> emit(Resource.Error(response.resultError))
        }

    }
    companion object {
        const val REQUEST_OK = 200
        const val NO_INTERNET = -1
    }
}
