package com.example.mvvmarchitecture.di

import android.util.Log
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.data.remote.ChatRepo
import com.example.mvvmarchitecture.data.remote.CommonRepo
import com.example.mvvmarchitecture.data.remote.NetworkUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun logger() = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun client(logger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(180, TimeUnit.SECONDS)
            readTimeout(180, TimeUnit.SECONDS)
        }.addInterceptor(logger)
            .build()
    }

    @Provides
    fun serialization() = GsonConverterFactory.create()

    @Provides
    fun retrofitBuilder(client: OkHttpClient, factory: GsonConverterFactory): Api =
        Retrofit.Builder()
            .baseUrl(NetworkUrl.BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(Api::class.java)

//    @Provides
//    fun provideRepo(api: Api): CommonRepo = CommonRepo(api)

    @Provides
    fun provideChatRepo(): Socket? {
        val opts = IO.Options()
        opts.forceNew = true
        opts.reconnection = true
        var socket: Socket? = null
        try {
            socket = IO.socket("http://99.79.15.95:8090/chat/", opts)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Log.e("Appmodule", Exception().stackTrace[0].methodName)
        }
        return socket
    }

    @Singleton
    @Provides
    fun provideRepo(socket: Socket?): ChatRepo = ChatRepo(socket)

    @Provides
    fun providesDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

}