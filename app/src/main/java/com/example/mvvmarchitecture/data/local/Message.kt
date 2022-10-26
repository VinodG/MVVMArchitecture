package com.example.mvvmarchitecture.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "body") val body: String,
    @ColumnInfo(name = "time") val time: String
)