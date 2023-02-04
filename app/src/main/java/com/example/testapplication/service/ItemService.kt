package com.example.testapplication.service

import android.content.Context
import com.clover.sdk.v3.inventory.InventoryConnector
import com.clover.sdk.v3.inventory.Modifier
import com.clover.sdk.v3.inventory.ModifierGroup
import com.clover.sdk.v3.order.OrderConnector
import com.example.testapplication.database.MainDatabase
import com.example.testapplication.database.entity.Item
import java.util.Date
import kotlin.random.Random

class ItemService(
    private val context: Context,
    private val orderConnector: OrderConnector,
    private val inventoryConnector: InventoryConnector
) {
    fun getAllItems(): List<Item> {
        return MainDatabase.getDatabase(context).itemDao().getAllItems()
    }

    fun saveItems(items: List<Item>) {
        MainDatabase.getDatabase(context).itemDao().insertItems(items)
    }

    fun updateItemPriceOnRandomPercentByOrderId(orderId: String, itemLineIds: List<String>) {
        val lineItems = orderConnector.getOrder(orderId).lineItems
        val updatedItems = mutableListOf<Item>()
        val percent = Random.nextInt(5, 26)

        lineItems
            .filter { itemLineIds.contains(it.id) }
            .distinctBy { it.taxRates[0].id }
            .forEach {
                val newPrice = it.price + it.price * (percent / 100)
                updatedItems.add(Item(Date().time, it.price.toDouble(), newPrice.toDouble(), orderId, it.id))
                if (it.modifications == null) {
                    val priceForAddToLineItem = ((percent * it.price / 100) * itemLineIds.size)
                    updateItemLinesOnScreen(percent, priceForAddToLineItem, orderId, it.id)
                }
                it.price = newPrice
            }
        if (updatedItems.isNotEmpty()) {
            saveItems(updatedItems)
        }
    }

    private fun updateItemLinesOnScreen(
        percent: Int,
        price: Long,
        orderId: String,
        lineItemId: String
    ) {
        val name = "Updating price by $percent%"
        val modifierGroup = inventoryConnector.createModifierGroup(ModifierGroup().setName(name))
        val modifier: Modifier = inventoryConnector
            .createModifier(modifierGroup.id, Modifier().setName(name).setPrice(price))
        orderConnector.addLineItemModification(orderId, lineItemId, modifier)
    }
}
