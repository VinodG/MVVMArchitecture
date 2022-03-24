package com.example.mvvmarchitecture.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    var userId: String? = null,
    var id: String? = null,
    var title: String? = null,
    var body: String? = null,
)