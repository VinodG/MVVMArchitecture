package com.example.mvvmarchitecture.di

import android.app.Application
import android.content.Context
import com.example.mvvmarchitecture.base.App
import com.example.mvvmarchitecture.base.AppViewModel
import com.example.mvvmarchitecture.base.SharedData
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.data.remote.NetworkUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    @Singleton
    @Provides
    fun provideCommonResource() = SharedData()

    @Singleton
    @Provides
    fun provideAppViewModel(@ApplicationContext context: Context) =
        AppViewModel(context = context as Application)

    @Provides
    fun retrofitBuilder(client: OkHttpClient, factory: GsonConverterFactory): Api =
        Retrofit.Builder()
            .baseUrl(NetworkUrl.BASE_URL)
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(Api::class.java)

}