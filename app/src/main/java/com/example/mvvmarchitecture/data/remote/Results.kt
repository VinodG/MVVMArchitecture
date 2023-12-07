package com.example.mvvmarchitecture.data.remote


sealed class Results {
    data class Error(
        val error: Exception
    ) : Results()

    data class Data<T>(val data: T) : Results()
    data class Empty(val data: String) : Results()
    data class Loading(val isLoading: Boolean) : Results()
}
//sealed interface Result<out T> {
//    data class Success<T>(val data: T) : Result<T>
//    data class Error(val exception: Throwable? = null) : Result<Nothing>
//    data object Loading : Result<Nothing>
//}

sealed class MyResult<out T> {
    object Loading : MyResult<Nothing>()
    data class Error(val throwable: Throwable? = null, val errorBody: String? = null) :
        MyResult<Nothing>()

    data class Success<T>(val data: T, val withNoContent: Boolean = false) : MyResult<T>()
}

/**
 * Error class to represent Http error.
 */
data class ApiError(
    val statusCode: Int,
    val msg: String? = null,
    val body: String? = null
) : Throwable()
