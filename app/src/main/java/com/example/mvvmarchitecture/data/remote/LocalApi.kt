package com.example.mvvmarchitecture.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LocalApi {

    @GET("json")
    suspend fun get(): ResponseBody

    @Multipart
    @POST("image")
    suspend fun postImage(
        @Part file: MultipartBody.Part,
        @Part("object") part: RequestBody
    ): ResponseBody
}


