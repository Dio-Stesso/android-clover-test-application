package com.example.testapplication.dependency.unit

import com.example.testapplication.service.ItemService
import org.koin.dsl.module

val itemService = module {
    single {
        ItemService()
    }
}