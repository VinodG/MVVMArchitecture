package com.example.mvvmarchitecture.data.remote


sealed class Results<T> {
    data class Error<T>(
        val error: Exception
    ) : Results<T>()

    data class Data<T>(val data: T) : Results<T>()
}
