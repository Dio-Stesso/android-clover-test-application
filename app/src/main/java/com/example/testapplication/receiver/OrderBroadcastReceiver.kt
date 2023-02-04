package com.example.testapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.clover.sdk.v1.Intents
import com.example.testapplication.receiver.event.NewItemLinesReceiver
import org.greenrobot.eventbus.EventBus

class OrderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        intent ?: return
        when (intent.action) {
            Intents.ACTION_LINE_ITEM_ADDED -> {
                val orderId = intent.getStringExtra(Intents.EXTRA_CLOVER_ORDER_ID)!!
                val lineItemIds = intent.getStringExtra(Intents.EXTRA_CLOVER_LINE_ITEM_ID)?.let { listOf(it) }
                        ?: intent.getStringArrayListExtra("com.clover.intent.extra.LINE_ITEM_IDS")
                        ?: listOf()
                if (lineItemIds.isNotEmpty()) {
                    EventBus.getDefault().post(NewItemLinesReceiver(orderId, lineItemIds))
                }
            }
        }
    }
}