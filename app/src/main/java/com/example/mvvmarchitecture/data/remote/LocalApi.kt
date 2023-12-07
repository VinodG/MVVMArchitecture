package com.example.mvvmarchitecture.data.remote

import com.example.mvvmarchitecture.data.models.Data
import com.example.mvvmarchitecture.data.models.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
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

    @GET("poly")
    suspend fun getPolyObject(): Response<Data>
    @GET("posts")
    suspend fun getPosts(): Response<List<Post>?>
    @GET("posts")
    suspend fun getResponseList(): Response<ResponseBody?>
}


