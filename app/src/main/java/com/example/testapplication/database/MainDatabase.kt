package com.example.testapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testapplication.database.dao.ItemDao
import com.example.testapplication.database.entity.Item

@Database(entities = [Item::class], version = 3, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}
