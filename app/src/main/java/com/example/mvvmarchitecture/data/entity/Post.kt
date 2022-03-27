package com.example.mvvmarchitecture.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var userId: Int? = null,
    var title: String? = null,
    var body: String? = null,
)