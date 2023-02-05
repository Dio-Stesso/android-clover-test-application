package com.example.testapplication.dependency.unit

import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.inventory.InventoryConnector
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val inventoryConnector = module {
    single {
        InventoryConnector(androidContext(), CloverAccount.getAccount(androidContext()), null).apply { connect() }
    }
}