package com.example.mvvmarchitecture.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Contact(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "name") val displayName: String,
    @ColumnInfo(name = "number") val phoneNumber: String,
    @ColumnInfo(name = "time") val timeStamp: String
)