package com.example.testapplication.dependency.unit

import androidx.room.Room
import com.example.testapplication.database.MainDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainDatabase = module {
    single {
        Room.databaseBuilder(androidContext().applicationContext, MainDatabase::class.java, "main_database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}