package com.example.mvvmarchitecture.data

import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Api
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommonRepo @Inject constructor(private var api: Api) {
    suspend fun get() = api.get()
    fun getFlow() = flow<List<Post>> {
        api.get()
    }
}