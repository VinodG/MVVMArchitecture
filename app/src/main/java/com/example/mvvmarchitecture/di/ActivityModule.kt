package com.example.mvvmarchitecture.di

import android.app.Activity
import android.content.Context
import com.example.mvvmarchitecture.base.PermissionUtils
import com.example.mvvmarchitecture.login.ui.PostAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun providePostAdapter() = PostAdapter(mutableListOf())

    @Provides
    fun providePermissionUtils(@ActivityContext context: Context) =
        PermissionUtils(context as Activity)
}
