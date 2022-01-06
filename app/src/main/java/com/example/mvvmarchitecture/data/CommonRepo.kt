package com.example.mvvmarchitecture.data

import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Api
import javax.inject.Inject

class CommonRepo @Inject constructor(private var api: Api) : Repo {
    override suspend fun getApi() = api.get()
}

class CommonRepo2 @Inject constructor(private var api: Api) : Repo {
    override suspend fun getApi() = listOf(Post(title = "vinod"))
}

interface Repo {
    suspend fun getApi(): List<Post>
}