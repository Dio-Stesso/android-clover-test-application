package com.example.testapplication.dependency.unit

import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.order.OrderConnector
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val orderConnector = module {
    single {
        OrderConnector(androidContext(), CloverAccount.getAccount(androidContext()), null).apply { connect() }
    }
}