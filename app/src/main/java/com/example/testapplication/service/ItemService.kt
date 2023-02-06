package com.example.testapplication.service

import com.clover.sdk.v3.inventory.InventoryConnector
import com.clover.sdk.v3.inventory.Modifier
import com.clover.sdk.v3.inventory.ModifierGroup
import com.clover.sdk.v3.order.OrderConnector
import com.example.testapplication.database.MainDatabase
import com.example.testapplication.database.entity.Item
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date
import kotlin.random.Random

class ItemService : KoinComponent {
    private val orderConnector: OrderConnector by inject()
    private val inventoryConnector: InventoryConnector by inject()
    private val mainDatabase: MainDatabase by inject()

    fun getAllItems(): List<Item> {
        return mainDatabase.itemDao().getAllItems()
    }

    fun saveItems(items: List<Item>) {
        mainDatabase.itemDao().insertItems(items)
    }

    fun updateItemPriceOnRandomPercentByOrderId(orderId: String, itemId: String) {
        val lineItems = orderConnector.getOrder(orderId).lineItems
        val updatedItems = mutableListOf<Item>()
        val percent = Random.nextInt(5, 26)

        lineItems
            .filter { it.id.equals(itemId) }
            .distinctBy { it.taxRates[0].id }
            .forEach {
                val newPrice = (it.price + it.price * (percent / 100.0)) / 100
                updatedItems.add(Item(Date().time, it.price.toDouble() / 100.0, newPrice, orderId, it.id))
                if (it.modifications == null) {
                    val priceForAddToLineItem = (((percent * it.price / 100.0)))
                    updateItemLinesOnScreen(percent, priceForAddToLineItem.toLong(), orderId, it.id)
                }
                it.price = newPrice.toLong()
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
