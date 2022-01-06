package com.example.mvvmarchitecture.data.remote

import com.example.mvvmarchitecture.data.models.Post
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET(NetworkUrl.URL)
    suspend fun get(): List<Post>

    @GET("invities/getmyevent/{profileid}/{currentime}")
    suspend fun getProfile(
        @Path("profileid") profileid: String,
        @Path("currentime") currentime: String
    ): Response<ResponseBody>

}


