package com.example.mvvmarchitecture.data.remote

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import java.io.EOFException

typealias SafeApiInvoke<T> = suspend () -> Response<T>

/**
 * Common gateway for all network API call.
 */
suspend inline fun <reified T : Any> safeNetworkCall(
    networkApiCall: SafeApiInvoke<T>
): MyResult<T> {
    return try {
        val response = networkApiCall()
        if (response.isSuccessful) {
            response.body()?.let { return@let MyResult.Success(it) }
                ?: run { tryToGetNoContentResult() }
        } else {
            return parseNetworkError(response)
        }
    } catch (ex: EOFException) {
        // Retrofit throws this exception when get 200 with no body.
        // (Weired response from our API)
        return tryToGetNoContentResult(ex)
    } catch (ex: Exception) {
        return MyResult.Error(ex)
    }
}

inline fun <reified T> tryToGetNoContentResult(ex: Exception? = null): MyResult<T> {
    Gson().fromJson("{}", T::class.java)?.let { // try if <T> is a object
        return MyResult.Success(it)
    } ?: Gson().fromJson("[]", T::class.java)?.let { // try if <T> is an array
        return MyResult.Success(it, withNoContent = true)
    } ?: return MyResult.Error(ex)
}

/**
 * Common function to parse different types of error.
 */
fun <T> parseNetworkError(response: Response<T>): MyResult.Error {
    val errorBody = response.errorBody()?.string()
    return MyResult.Error(errorBody = errorBody)
}


fun <T> MyResult<T>.asResult(): Flow<MyResult<T>> {
    return flow { emit(this@asResult) }.onStart { emit(MyResult.Loading) }
        .catch { emit(MyResult.Error(it)) }
}