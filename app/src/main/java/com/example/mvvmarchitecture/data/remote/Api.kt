package com.example.mvvmarchitecture.data.remote

import com.example.mvvmarchitecture.data.models.Post
import okhttp3.ResponseBody
import retrofit2.http.GET

interface Api {
    @GET(NetworkUrl.URL)
    suspend fun get(): List<Post>
}


