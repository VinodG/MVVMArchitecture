package com.example.mvvmarchitecture.temp

import com.example.mvvmarchitecture.data.models.Post
import javax.inject.Inject

class TempRepo @Inject constructor() {
    fun getApi() = mutableListOf<Post>().apply {
        add(Post(userId = "1", id = "12", title = "title"))
    }
}