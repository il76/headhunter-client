package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.VacancyDbConverter
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.models.VacancySearchResult

class VacancyLocalRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val vacancyDbConverter: VacancyDbConverter
) : VacancyLocalRepository {
    override fun getAll(): Flow<Result<VacancySearchResult>> {
        return appDatabase.vacancyDao().getVacancies()
            .map { vacancies ->
                val vacancyList = vacancies.map { vacancyDbConverter.map(it) }
                Result.success(
                    VacancySearchResult(
                        vacancies = vacancyList,
                        found = vacancyList.size
                    )
                )
            }
            .catch { e ->
                emit(Result.failure(Exception("Failed to load vacancies: ${e.message}")))
            }
    }
}
