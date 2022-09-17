package com.example.mvvmarchitecture.firebase.models

import kotlinx.coroutines.flow.Flow

interface BaseDataSource<T> {
    fun add(data: T): Flow<ApiState<T>>
    fun onChange(): Flow<ApiState<out List<T>>>
}