package ru.practicum.android.diploma.data.network

import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancyDetailsRequest
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val hhService: HHApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return when (dto) {
            is VacancySearchRequest -> makeRequest {
                hhService.getVacancies(
                    dto.text,
                    dto.page.toString(),
                    area = dto.area,
                    salary = dto.salary,
                    onlyWithSalary = dto.onlyWithSalary
                )
            }
            is VacancyDetailsRequest -> makeRequest { hhService.getVacancyDetails(dto.vacancyId) }
            is IndustriesRequest -> makeRequest { hhService.getIndustries() }
            else -> Response().apply {
                resultCode = BAD_REQUEST_CODE
                resultError = "Invalid request type"
            }
        }
    }

    private suspend fun makeRequest(apiCall: suspend () -> Response): Response {
        return try {
            val data = apiCall()
            data.apply {
                resultCode = HTTP_SUCCESS_CODE
                resultError = ""
            }
        } catch (e: IOException) {
            Response().apply {
                resultCode = NETWORK_ERROR_CODE
                resultError = "Network error: ${e.localizedMessage}"
            }
        } catch (e: HttpException) {
            Response().apply {
                resultCode = e.code()
                resultError = "HTTP error: ${e.message()}"
            }
        }
    }

    companion object {
        private const val HTTP_SUCCESS_CODE = 200
        private const val NETWORK_ERROR_CODE = 0
        private const val BAD_REQUEST_CODE = 400
        private const val SERVER_ERROR_CODE = 500
    }
}
