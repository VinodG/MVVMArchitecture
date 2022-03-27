package com.example.mvvmarchitecture.di

import android.content.Context
import androidx.annotation.StringRes
import com.example.mvvmarchitecture.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceManager @Inject constructor(@ApplicationContext var context: Context) {
    fun getString(@StringRes id: Int) = context.getString(R.string.app_name)
}