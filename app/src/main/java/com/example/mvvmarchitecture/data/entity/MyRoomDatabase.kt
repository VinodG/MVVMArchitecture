package com.example.mvvmarchitecture.data.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Post::class], version = 2, exportSchema = true)
abstract class MyRoomDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        private var INSTANCE: MyRoomDatabase? = null

        fun getDatabase(context: Context): MyRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MyRoomDatabase::class.java,
                    "post_database.db"
                ).fallbackToDestructiveMigration() // this
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}