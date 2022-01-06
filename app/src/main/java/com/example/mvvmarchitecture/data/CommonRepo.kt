package com.example.mvvmarchitecture.data

import com.example.mvvmarchitecture.data.remote.Api
import javax.inject.Inject

class CommonRepo @Inject constructor(private var api: Api) {
    suspend fun get() = api.get()
    suspend fun getProfile() = api.getProfile("61adae0c6c6e5100efbaa41f","1640847949701")
}