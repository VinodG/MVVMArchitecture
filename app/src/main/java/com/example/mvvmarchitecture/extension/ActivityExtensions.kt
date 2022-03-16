package com.example.mvvmarchitecture.extension

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

fun Activity.performOnInternet(onNoInternet: (() -> Unit)? = null, onInternet: () -> Unit) {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (cm.activeNetworkInfo?.isConnected == true) {
        onInternet()
    } else
        onNoInternet?.invoke()
}

fun Activity.toast(msg: String) {
    if (!msg.isNullOrBlank())
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}