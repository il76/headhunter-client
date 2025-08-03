package ru.practicum.android.diploma.data.db

import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyDbConverter {
    fun map(vacancy: Vacancy): VacancyEntity {
        return VacancyEntity(
            id = vacancy.id,
            name = vacancy.name,
            logoUrl = vacancy.logoUrl,
            areaName = vacancy.areaName,
            employerName = vacancy.employerName,
            salaryCurrency = vacancy.salaryCurrency,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            experience = vacancy.experience,
            employment = vacancy.employment,
            description = vacancy.description,
            keySkills = vacancy.keySkills
        )
    }

    fun map(vacancy: VacancyEntity): Vacancy {
        return Vacancy(
            id = vacancy.id,
            name = vacancy.name,
            logoUrl = vacancy.logoUrl,
            areaName = vacancy.areaName,
            employerName = vacancy.employerName,
            salaryCurrency = vacancy.salaryCurrency,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            experience = vacancy.experience,
            employment = vacancy.employment,
            description = vacancy.description,
            keySkills = vacancy.keySkills
        )
    }
}
