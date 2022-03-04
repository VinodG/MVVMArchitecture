package com.example.mvvmarchitecture.multilevel

import java.lang.Exception

sealed class Network {
    data class Data(var data: List<Product>) : Network()
    data class Error(var exception: Exception) : Network()
    object Empty : Network()
}