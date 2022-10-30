package com.example.mvvmarchitecture.di

import android.app.Activity
import android.content.Context
import com.example.mvvmarchitecture.base.PermissionUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext


@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {


    @Provides
    fun providePermissionUtils(@ActivityContext context: Context) =
        PermissionUtils(context as Activity)
}
