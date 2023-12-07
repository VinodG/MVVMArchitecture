package com.example.mvvmarchitecture.di

import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.Repo
import com.example.mvvmarchitecture.data.models.Base
import com.example.mvvmarchitecture.data.models.Image
import com.example.mvvmarchitecture.data.models.Item
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.data.remote.LocalApi
import com.example.mvvmarchitecture.data.remote.NetworkUrl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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

    @Provides
    fun retrofitBuilderForLocal(client: OkHttpClient, factory: GsonConverterFactory): LocalApi =
        Retrofit.Builder()
            .baseUrl(NetworkUrl.LOCAL_BASE_URL)
            .client(client)
            .addConverterFactory(factory)
//            .addConverterFactory(Json {
//                ignoreUnknownKeys = true
//                coerceInputValues = true
//                this.serializersModule = modules
//            }.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(LocalApi::class.java)

    @Provides
    fun provideRepo(api: Api, localApi: LocalApi): Repo = CommonRepo(api, localApi)

    @Provides
    fun providesDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    val modules = SerializersModule {
        polymorphic(Base::class, Item::class , Item.serializer())
        polymorphic(Base::class, Image::class , Image.serializer())
    }

}