package com.example.mvvmarchitecture.firebase.models

sealed interface ApiState<T> {
    class Loader<T>() : ApiState<T>
    data class Success<T>(var data: T) : ApiState<T>
    class Error<T>(var throwable: Throwable) : ApiState<T>
}
