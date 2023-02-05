package com.example.testapplication.database.entity

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity(tableName = "items")
data class Item(
    @ColumnInfo(name = "date") var date: Long,
    @ColumnInfo(name = "old_price") var oldPrice: Double,
    @ColumnInfo(name = "new_price") var newPrice: Double,
    @ColumnInfo(name = "order_id") var orderId: String,
    @ColumnInfo(name = "item_id") var itemId: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @SuppressLint("SimpleDateFormat")
    fun getCorrectDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(date)
    }

    fun getCertainOldPrice(): String {
        return "$oldPrice$"
    }

    fun getCertainNewPrice(): String {
        return "$newPrice$"
    }
}
