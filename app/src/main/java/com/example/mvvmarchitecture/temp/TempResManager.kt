package com.example.mvvmarchitecture.temp

import androidx.annotation.StringRes
import javax.inject.Inject

class TempResManager @Inject constructor() {
    fun getString(@StringRes id: Int) = "testing"
}