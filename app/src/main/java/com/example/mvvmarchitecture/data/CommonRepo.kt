package com.example.mvvmarchitecture.data

import com.example.mvvmarchitecture.data.remote.Api
import javax.inject.Inject

class CommonRepo @Inject constructor(private var api: Api) {
    suspend fun get() = api.get()
}