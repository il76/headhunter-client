package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.VacancyRepositoryImpl.Companion.NO_INTERNET
import ru.practicum.android.diploma.data.VacancyRepositoryImpl.Companion.REQUEST_OK
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.api.IndustriesRepository
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.domain.models.IndustrySearchResult
import ru.practicum.android.diploma.util.Resource

class IndustriesRepositoryImpl(
    private val networkClient: NetworkClient
) : IndustriesRepository {
    override fun getIndustries(): Flow<Resource<IndustrySearchResult>> = flow {
        val response = networkClient.doRequest(IndustriesRequest())
        when (response.resultCode) {
            NO_INTERNET -> emit(Resource.Error("Check connection to internet"))
            REQUEST_OK -> {
                val searchResponse = response as IndustriesResponse
                emit(
                    Resource.Success(
                        IndustrySearchResult(
                            industrys = searchResponse.industries.map {
                                Industry(
                                    id = it.id,
                                    name = it.name
                                )
                            },
                            found = searchResponse.industries.size
                        )
                    )
                )
            }
            else -> emit(Resource.Error(response.resultError))
        }
    }

}
