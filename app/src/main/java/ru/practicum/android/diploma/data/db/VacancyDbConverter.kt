package ru.practicum.android.diploma.data.db

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.dto.Contacts
import ru.practicum.android.diploma.data.dto.Phone
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyDbConverter(
    private val gson: Gson
) {
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
            keySkills = vacancy.keySkills,
            schedule = vacancy.schedule,
            contactEmail = vacancy.contacts?.email,
            contactName = vacancy.contacts?.name,
            phonesJson = vacancy.contacts?.phones?.let { gson.toJson(it) },
        )
    }

    fun map(vacancy: VacancyEntity): Vacancy {
        val phones = try {
            gson.fromJson<List<Phone>>(
                vacancy.phonesJson,
                object : TypeToken<List<Phone>>() {}.type
            )
        } catch (e: JsonSyntaxException) {
            Log.w("VacancyDbConverter", "Failed to parse phones", e) // detekt!
            null
        } catch (e: JsonParseException) {
            Log.w("VacancyDbConverter", "Failed to parse phones", e) // detekt!
            null
        }
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
            keySkills = vacancy.keySkills,
            schedule = vacancy.schedule,
            contacts = if (vacancy.contactEmail != null || vacancy.contactName != null || phones != null) {
                Contacts(
                    email = vacancy.contactEmail ?: "",
                    name = vacancy.contactName ?: "",
                    phones = phones
                )
            } else {
                null
            }
        )
    }
}
