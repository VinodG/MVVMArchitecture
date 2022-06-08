package com.example.orders

import java.io.Serializable

data class ProductInOrder(
    var id: String,
    var name: String
) : Serializable