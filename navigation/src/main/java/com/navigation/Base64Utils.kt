package com.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
public fun String.encodedString(): String? {
    return Base64.getUrlEncoder()?.encodeToString(this.toByteArray())
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.decodedString(): String {
    return String(Base64.getUrlDecoder().decode(this))
}

fun <T> T.toStr(): String? {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromStr(): T {
    return Gson().fromJson(this, T::class.java)
}


@RequiresApi(Build.VERSION_CODES.O)
fun <T> T.toEncodedBase64(): String? {
    return Gson().toJson(this).encodedString()
}

@RequiresApi(Build.VERSION_CODES.O)
inline fun <reified T> String.toDecodedBase64(): T {
    return Gson().fromJson(this.decodedString(), T::class.java)
}
