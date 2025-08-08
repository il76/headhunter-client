package ru.practicum.android.diploma.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.dto.Phone

@ProvidedTypeConverter
class Converters(private val gson: Gson) {

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toList(json: String?): List<String>? {
        return json?.let {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(it, type)
        }
    }

    @TypeConverter
    fun fromPhoneList(phones: List<Phone>?): String? {
        return phones?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toPhoneList(phonesJson: String?): List<Phone>? {
        return phonesJson?.let {
            gson.fromJson(it, object : TypeToken<List<Phone>>() {}.type)
        }
    }
}
