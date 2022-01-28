package com.example.mvvmarchitecture.list.data

import com.example.mvvmarchitecture.data.Repo
import com.example.mvvmarchitecture.data.models.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FakeRepo : Repo {
    override suspend fun getApi(): List<Post> {
        var list = mutableListOf<Post>()
//        delay(1)
        repeat(10) {
            list.add(Post("user-$it", "$it", "title", "body"))
        }
        return list
    }
}