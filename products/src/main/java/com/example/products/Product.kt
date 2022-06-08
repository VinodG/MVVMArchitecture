package com.example.products

import java.io.Serializable

data class Product(
    var id: String,
    var name: String
) : Serializable