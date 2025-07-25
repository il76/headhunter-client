package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacancySearchRequest
import java.io.IOException

class RetrofitNetworkClient(
    private val hhService: HHApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is VacancySearchRequest) {
            return Response().apply {
                resultCode = BAD_REQUEST_CODE
                resultError = "Invalid request type"
            }
        }

        return try {
            val response = withContext(Dispatchers.IO) {
                hhService.getVacancies(dto.text)
            }
            response.apply {
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
                resultError = "HTTP error: ${e.message}"
            }
        }
// detekt не пропускает этот блок! И как ловить неопределённые исключения?
// пока оставлю этот комментарий до момента фактической реализации
//        } catch (e: Exception) {
//            Response().apply {
//                resultCode = SERVER_ERROR_CODE
//                resultError = "Unexpected error: ${e.localizedMessage}"
//            }
//        }
    }

    companion object {
        private const val HTTP_SUCCESS_CODE = 200
        private const val NETWORK_ERROR_CODE = 0
        private const val BAD_REQUEST_CODE = 400
        private const val SERVER_ERROR_CODE = 500
    }
}
