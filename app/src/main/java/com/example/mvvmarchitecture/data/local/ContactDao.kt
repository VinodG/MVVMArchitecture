package com.example.mvvmarchitecture.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    fun getAll(): Flow<List<Contact>>

    @Query("SELECT * FROM contact WHERE id IN (:msgId)")
    fun loadAllByIds(msgId: IntArray): List<Contact>

    @Insert
    fun insertAll(vararg msgs: Contact)

    @Update
    fun update(vararg msgs: Contact)

    @Delete
    fun delete(msg: Contact)
}