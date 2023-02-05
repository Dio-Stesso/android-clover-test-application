package com.example.testapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.clover.sdk.v1.Intents
import com.example.testapplication.service.ItemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OrderBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val itemService: ItemService by inject()

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intents.ACTION_LINE_ITEM_ADDED -> {
                val orderId = intent.getStringExtra(Intents.EXTRA_CLOVER_ORDER_ID)!!
                val lineItemIds = intent.getStringExtra(Intents.EXTRA_CLOVER_LINE_ITEM_ID)?.let { listOf(it) }
                    ?: intent.getStringArrayListExtra("com.clover.intent.extra.LINE_ITEM_IDS")
                    ?: listOf()
                if (lineItemIds.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        itemService.updateItemPriceOnRandomPercentByOrderId(orderId, lineItemIds)
                    }
                }
            }
        }
    }
}