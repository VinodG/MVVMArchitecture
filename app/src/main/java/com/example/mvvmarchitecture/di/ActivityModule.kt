package com.example.mvvmarchitecture.di

import com.example.mvvmarchitecture.login.ui.PostAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    fun providePostAdapter() = PostAdapter(mutableListOf())
}