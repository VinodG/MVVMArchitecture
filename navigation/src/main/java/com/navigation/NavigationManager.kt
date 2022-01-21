package com.example.orders

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.navigation.toDecodedBase64
import com.navigation.toEncodedBase64

object NavigationManager {
    const val ORDER = "app://order/orders"
    const val ORDER_DETAIL = "app://order/details"
    const val PRODUCT = "app://product/products"
    const val PRODUCT_DETAIL = "app://product/details"
}


@RequiresApi(Build.VERSION_CODES.O)
fun <T> Fragment.navigate(url: String, arg: T) {
    var request = NavDeepLinkRequest.Builder.fromUri(
        "$url?args=${arg.toEncodedBase64()}".toUri()
    ).build()
    findNavController().navigate(request)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Fragment.navigate(url: String) {
    var request = NavDeepLinkRequest.Builder.fromUri(url.toUri()).build()
    findNavController().navigate(request)
}

@RequiresApi(Build.VERSION_CODES.O)
inline fun <reified T> Fragment.args() =
    (arguments?.get(
        arguments?.keySet()?.firstOrNull() ?: ""
    ) as Intent).data?.getQueryParameter("args")?.toDecodedBase64<T>()