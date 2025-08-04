package ru.practicum.android.diploma.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.VacancyDbConverter
import ru.practicum.android.diploma.domain.api.VacancyLocalRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancySearchResult
import ru.practicum.android.diploma.util.Resource

class VacancyLocalRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val vacancyDbConverter: VacancyDbConverter
) : VacancyLocalRepository {
    override fun getAll(): Flow<Resource<VacancySearchResult>> {
        return appDatabase.vacancyDao().getVacancies()
            .map { vacancies ->
                val vacancyList = vacancies.map { vacancyDbConverter.map(it) }
                Resource.Success(
                    VacancySearchResult(
                        vacancies = vacancyList,
                        found = vacancyList.size
                    )
                ) as Resource<VacancySearchResult> // Явное приведение типа, без него catch ругается
            }
            .catch { e ->
                emit(Resource.Error("Failed to load vacancies: ${e.message}"))
            }
    }

    override fun getVacancyDetails(vacancyId: String): Flow<Resource<Vacancy>> {
        return appDatabase.vacancyDao().getVacancyDetails(vacancyId)
            .map { vacancyEntity ->
                Resource.Success(vacancyDbConverter.map(vacancyEntity)) as Resource<Vacancy>
            }
            .catch { e ->
                emit(Resource.Error("Failed to load vacancy details: ${e.message}"))
            }
    }
}
