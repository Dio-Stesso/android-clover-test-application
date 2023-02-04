package com.example.testapplication

import android.accounts.Account
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.inventory.InventoryConnector
import com.clover.sdk.v3.order.OrderConnector
import com.example.testapplication.adapter.ItemAdapter
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.receiver.event.NewItemLinesReceiver
import com.example.testapplication.service.ItemService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var managerBinding: RecyclerView.LayoutManager

    private lateinit var itemService: ItemService

    private var account: Account? = null
    private var orderConnector: OrderConnector? = null
    private var inventoryConnector: InventoryConnector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        account = CloverAccount.getAccount(this)
        checkingCloverAccount()
        connectToCloverOrder()
        connectToCloverInventory()

        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        managerBinding = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        itemService = ItemService(applicationContext, orderConnector!!, inventoryConnector!!)
        val items = itemService.getAllItems()
        activityBinding.rvItemList.apply {
            adapter = ItemAdapter(items)
            layoutManager = managerBinding
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromCloverOrder()
        disconnectFromCloverInventory()
        EventBus.getDefault().unregister(this)
    }

    private fun connectToCloverOrder() {
        disconnectFromCloverOrder()
        if (account != null) {
            orderConnector = OrderConnector(this, account, null)
            orderConnector!!.connect()
        }
    }

    private fun disconnectFromCloverOrder() {
        if (orderConnector != null) {
            orderConnector!!.disconnect()
            orderConnector = null
        }
    }

    private fun connectToCloverInventory() {
        disconnectFromCloverInventory()
        if (account != null) {
            inventoryConnector = InventoryConnector(this, account, null)
            inventoryConnector!!.connect()
        }
    }

    private fun disconnectFromCloverInventory() {
        if (inventoryConnector != null) {
            inventoryConnector!!.disconnect()
            inventoryConnector = null
        }
    }

    private fun checkingCloverAccount() {
        if (account == null) {
            account = CloverAccount.getAccount(this)

            if (account == null) {
                return
            }
        }
    }

    @Subscribe
    fun handelRequestFromOrderReceiver(event: NewItemLinesReceiver) {
        itemService.updateItemPriceOnRandomPercentByOrderId(
            orderId = event.orderId,
            itemLineIds = event.itemLines
        )
    }
}