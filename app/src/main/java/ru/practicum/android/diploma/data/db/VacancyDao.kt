package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity)

    @Query("DELETE FROM vacancies WHERE id = :vacancyId")
    suspend fun deleteVacancyById(vacancyId: Int)

    @Query("SELECT * FROM vacancies WHERE id = :vacancyId")
    fun getVacancyDetails(vacancyId: String): Flow<VacancyEntity>

    @Query("SELECT * FROM vacancies ORDER BY id DESC")
    fun getVacancies(): Flow<List<VacancyEntity>>
}
