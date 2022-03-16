package com.example.mvvmarchitecture.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {

    @GET
    fun get(@Url url: String): ResponseBody
}


