package com.example.testapplication.receiver.event

data class NewItemLinesReceiver(
    val orderId: String,
    val itemLines: List<String>
)
