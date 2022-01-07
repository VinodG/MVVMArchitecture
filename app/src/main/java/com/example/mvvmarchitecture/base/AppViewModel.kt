package com.example.mvvmarchitecture.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import javax.inject.Inject
import javax.inject.Singleton

class AppViewModel(context: Application) :
    AndroidViewModel(context as Application) {
    var x: Int = 1
}

@Singleton
class SharedData @Inject constructor() {
    var x: Int = 0
}