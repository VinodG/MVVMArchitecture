package com.example.mvvmarchitecture.login.ui

import com.example.mvvmarchitecture.data.entity.Post
import com.example.mvvmarchitecture.data.entity.PostDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepository @Inject constructor(private var db: PostDao) {
    suspend fun getPosts() = withContext(Dispatchers.IO) {
        db.getPosts()
    }

    suspend fun insert(post: Post) = withContext(Dispatchers.IO) {
        db.insert(post = post)
    }
}