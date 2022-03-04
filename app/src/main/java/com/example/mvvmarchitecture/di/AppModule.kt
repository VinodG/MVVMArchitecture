package com.example.mvvmarchitecture.di

import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.Repo
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.data.remote.NetworkUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    fun provideRepo(api: Api): Repo = CommonRepo(api)

    @Provides
    fun providesDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

}