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

class OrderBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val itemService: ItemService = ItemService()

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intents.ACTION_LINE_ITEM_ADDED -> {
                val orderId = intent.getStringExtra(Intents.EXTRA_CLOVER_ORDER_ID)!!
                val itemId = intent.getStringExtra(Intents.EXTRA_CLOVER_LINE_ITEM_ID)
                    ?: intent.getStringArrayListExtra("com.clover.intent.extra.LINE_ITEM_IDS")
                if (itemId !== null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        itemService.updateItemPriceOnRandomPercentByOrderId(orderId, itemId as String)
                    }
                }
            }
        }
    }
}