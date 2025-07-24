package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import ru.practicum.android.diploma.data.dto.VacancySearchResponse
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.Vacancy

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
}
