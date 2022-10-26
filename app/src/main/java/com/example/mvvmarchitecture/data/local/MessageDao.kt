package com.example.mvvmarchitecture.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    fun getAll(): Flow<List<Message>>

    @Query("SELECT * FROM message WHERE id IN (:msgId)")
    fun loadAllByIds(msgId: IntArray): List<Message>

    @Insert
    fun insertAll(vararg msgs: Message)

    @Update
    fun update(vararg msgs: Message)

    @Delete
    fun delete(msg: Message)
}