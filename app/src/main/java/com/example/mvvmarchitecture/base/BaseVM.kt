package com.example.mvvmarchitecture.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseVM :ViewModel(){
    val lvLoader: MutableLiveData<Boolean> = MutableLiveData()
    fun showLoader() = lvLoader.postValue(true)
    fun hideLoader() = lvLoader.postValue(false)
}