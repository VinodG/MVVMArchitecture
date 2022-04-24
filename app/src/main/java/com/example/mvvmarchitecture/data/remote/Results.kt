package com.example.mvvmarchitecture.data.remote


sealed class Results {
    data class Error(
        val error: Exception
    ) : Results()

    data class Data<T>(val data: T) : Results()
    data class Empty(val data: String) : Results()
    data class Loading(val isLoading: Boolean=true) : Results()
}
