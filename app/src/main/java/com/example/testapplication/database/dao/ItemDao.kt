package com.example.testapplication.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.testapplication.database.entity.Item

@Dao
interface ItemDao {
    @Query(value = "SELECT * FROM items ORDER BY DATE")
    fun getAllItems(): List<Item>

    @Insert
    fun insertItems(items: List<Item>)
}