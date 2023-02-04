package com.example.testapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testapplication.database.dao.ItemDao
import com.example.testapplication.database.entity.Item

@Database(entities = [Item::class], version = 3, exportSchema = false)
abstract class MainDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            //val tempInstance = INSTANCE
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            return if (INSTANCE != null) {
                INSTANCE as MainDatabase
            } else {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    "main_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
