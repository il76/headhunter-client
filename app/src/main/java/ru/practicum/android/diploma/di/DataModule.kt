package ru.practicum.android.diploma.di

import android.content.Context
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        androidContext()
            .getSharedPreferences("hh_preferences", Context.MODE_PRIVATE)
    }
    factory {
        Gson()
    }
}
