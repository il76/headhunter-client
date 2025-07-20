package ru.practicum.android.diploma.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VacancyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyEntity) // до реализации классов, будет VacancyEntity

    @Query("DELETE FROM vacancies WHERE id = :vacancyId")
    suspend fun deleteVacancyById(vacancyId: Int)

    @Query("SELECT * FROM vacancies ORDER BY id DESC")
    suspend fun getVacancies(): List<VacancyEntity>
}
