package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

val dataModule = module {
    single {
        androidContext()
            .getSharedPreferences("hh_preferences", Context.MODE_PRIVATE)
    }
    factory {
        Gson()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<HHApiService> {
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApiService::class.java)
    }

    single {
        databaseBuilder(androidContext(), AppDatabase::class.java, "hh.db")
            .fallbackToDestructiveMigration() // на время отладки, чтобы не писать постоянно миграции
            .build()
    }
}
