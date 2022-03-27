package com.example.mvvmarchitecture.data.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(post: Post)

    @Query("SELECT * FROM POST ORDER BY ID ASC")
    fun getPosts(): MutableList<Post>

}