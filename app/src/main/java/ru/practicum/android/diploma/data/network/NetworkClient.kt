package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    suspend fun doRequestForIndustries(dto: IndustriesRequest): Response
}
