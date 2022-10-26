package com.example.mvvmarchitecture.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Message::class, Contact::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun contactDao(): ContactDao
}
