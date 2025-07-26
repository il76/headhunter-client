package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.VacancyDetailsResponse
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetailsState

class VacancyRepositoryImpl(private val networkClient: NetworkClient) : VacancyRepository {

    override fun search(query: String, page: Int): Flow<List<Vacancy>?> = flow {
        val response = networkClient.doRequest(VacancySearchRequest(query, page = page))
        if (response.resultError == "") {
            with(response as VacancySearchResponse) {
                val data = results?.map { vacancy ->
                    Vacancy(
                        "", // временно, нужно разобраться, как его подставить из HH
                        vacancy.name,
                        vacancy.employer.logoUrls.size90.toString(),
                        vacancy.area.name,
                        vacancy.employer.name,
                        vacancy.salary?.currency ?: "",
                        vacancy.salary?.from,
                        vacancy.salary?.to,
                        vacancy.experience.name,
                        vacancy.employment.name,
                        vacancy.description,
                        vacancy.keySkills.map { it.name },
                    )
                }
                emit(data)
            }
        } else {
            emit(null)
        }
    }

    override fun getVacancyDetails(vacancyId: String): Flow<VacancyDetailsState> = flow {
        val response = networkClient.doRequest(VacancyDetailsRequest(vacancyId))
        when (response.resultCode) {
            Response.SUCCESS_RESPONSE_CODE -> {
                val result = response as VacancyDetailsResponse
                val vacancy = result.vacancy
                if (vacancy.id.isEmpty()) {
                    emit(VacancyDetailsState.EmptyState)
                } else {
                    val data = Vacancy(
                        vacancy.id,
                        vacancy.name,
                        vacancy.logoUrl,
                        vacancy.areaName,
                        vacancy.employerName,
                        vacancy.salaryCurrency,
                        vacancy.salaryFrom,
                        vacancy.salaryTo,
                        vacancy.experience,
                        vacancy.employment,
                        vacancy.description,
                        vacancy.keySkills,
                    )
                    emit(VacancyDetailsState.ContentState(data))
                }
            }

            Response.NO_INTERNET_ERROR_CODE -> {
                emit(VacancyDetailsState.ConnectionError)
            }

            Response.BAD_REQUEST_ERROR_CODE, Response.NOT_FOUND_ERROR_CODE -> {
                emit(VacancyDetailsState.EmptyState)
            }

            else -> {
                emit(VacancyDetailsState.ServerError)
            }
        }
    }.flowOn(Dispatchers.IO)

}
