package com.example.mvvmarchitecture.data.remote

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import java.io.EOFException

inline fun <reified T> safeApi(
    networkApiCall: () -> Response<T>
): MyResult<T> {
    return try {
        val response = networkApiCall()
        val body = response.body()
        val isSuccess = response.isSuccessful
        when {
            isSuccess && body != null -> {
                Log.e("safeApi", "isSuccess: true and body not null ${body.toString()}")
                MyResult.Success(body)
            }
            isSuccess -> {
                Log.e("safeApi", "isSuccess: true ")
                tryToGetNoContentResult()
            }
            else -> {
                Log.e("safeApi", "parseNetworkError: else ")
                //for error code 502..etc
                parseNetworkError(response)
            }
        }
    } catch (ex: EOFException) {
        Log.e("safeApi", "EOF tryToGetNoContentResult: ")
        //if response type is not Response<Response> and returned response is null (or) without response body
        tryToGetNoContentResult()
    } catch (ex: Exception) {
        Log.e("safeApi", "exception: ")
        //for wrong response -> illegalStatementException
        MyResult.Error(ex)
    }
}

inline fun <reified T> tryToGetNoContentResult(): MyResult<T> {
    return when (T::class.java) {
        List::class.java ->
            MyResult.Success(Gson().fromJson("[]", T::class.java), withNoContent = true)

        else ->
            MyResult.Success(Gson().fromJson("{}", T::class.java), withNoContent = true)
    }
}

fun <T> parseNetworkError(response: Response<T>): MyResult.Error {
    val errorBody = response.errorBody()?.string()
    return MyResult.Error(errorBody = errorBody)
}


fun <T> MyResult<T>.asResult(): Flow<MyResult<T>> {
    return flow { emit(this@asResult) }.onStart { emit(MyResult.Loading) }
        .catch { emit(MyResult.Error(it)) }
}
