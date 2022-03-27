package com.example.mvvmarchitecture.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    var userId: Int? = null,
    var id: Int? = null,
    var title: String? = null,
    var body: String? = null,
)