package com.example.mvvmarchitecture.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CustomInterceptor(var key: String) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

//        var preference = EntryPoints.get(context, EntryPointManager::class.java).getPref()

        val request: Request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $key")
            .build()
        println("CustomInterceptor: context : ${key}, url: ${request.url}")


        //try the request
        val response: Response = chain.proceed(request)
        //Log.e("TESTING", "intercept: ", )
        /* if (response.isSuccessful) {
             // close previous response
             response.close()
             // get a new token (I use a synchronous Retrofit call)
             Log.e(TAG, "intercept: got response ${Thread.currentThread().name}")

             // create a new request and modify it accordingly using the new token
             var newRequest: Request = request.newBuilder().build();
             var res =
                 retrofit.refreshToken(token = "Bearer $token").execute()
             Log.e(TAG, "intercept: ${res.errorBody()?.string()}")
             if (res.code() == 401)
                 hrow TokenExpiredException("Token Expired")
             // retry the request
             return chain.proceed(newRequest);
         } else {
             Log.e(TAG, "intercept: response is not successfull ")
         }*/

        // otherwise just pass the original response on
//        var url = request.url.toUrl().toString()
//        println("url: ${url}")
//        if (url.contains("event"))
//            throw TokenExpiredException("Token Expired")

        if (response.code == 401) {
            Log.e("401", "intercept: Key --> ${key}, url: ${request.url},")
        }
//
//        if (response.code == 404)
//            Log.e("404", "intercept: URL --> ${request.url}")

        return response;
    }


}

