package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.Converters
import ru.practicum.android.diploma.data.db.VacancyDbConverter
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.ui.sharing.ExternalNavigator
import ru.practicum.android.diploma.ui.sharing.ExternalNavigatorImpl
import ru.practicum.android.diploma.util.APP_DATA_BASE
import ru.practicum.android.diploma.util.APP_SHARED_PREFS
import ru.practicum.android.diploma.util.HH_BASE_URL

val dataModule = module {
    single {
        androidContext()
            .getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE)
    }

    factory {
        Gson()
    }

    single { Converters(get()) }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>()) // Логирование
            .build()
    }

    single<HHApiService> {
        Retrofit.Builder()
            .baseUrl(HH_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApiService::class.java)
    }

    single {
        databaseBuilder(androidContext(), AppDatabase::class.java, APP_DATA_BASE)
            .fallbackToDestructiveMigration() // на время отладки, чтобы не писать постоянно миграции
            .addTypeConverter(get<Converters>())
            .build()
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(
            context = get()
        )
    }
    single {
        VacancyDbConverter(get())
    }
}
