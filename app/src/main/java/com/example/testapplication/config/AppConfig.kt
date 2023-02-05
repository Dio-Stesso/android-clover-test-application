package com.example.testapplication.config

import android.app.Application
import com.example.testapplication.dependency.unit.mainDatabase
import com.example.testapplication.dependency.unit.inventoryConnector
import com.example.testapplication.dependency.unit.itemService
import com.example.testapplication.dependency.unit.orderConnector
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(inventoryConnector, orderConnector, mainDatabase, itemService))
        }
    }
}