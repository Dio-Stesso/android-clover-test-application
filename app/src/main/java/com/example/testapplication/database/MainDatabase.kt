package com.example.testapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.testapplication.database.dao.ItemDao
import com.example.testapplication.database.entity.Item
import org.koin.core.component.KoinComponent

@Database(entities = [Item::class], version = 3, exportSchema = false)
abstract class MainDatabase : RoomDatabase(), KoinComponent {
    abstract fun itemDao(): ItemDao

    companion object {
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
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
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
