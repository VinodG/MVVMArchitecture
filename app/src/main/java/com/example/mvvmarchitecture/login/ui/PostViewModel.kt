package com.example.mvvmarchitecture.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.NetworkUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor() : ViewModel() {
    fun filter(str: String) {

    }

    fun getPost() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            }
            val client = HttpClient(Android) {
                install(Logging) {
                    level = LogLevel.ALL
                }
                install(JsonFeature) {
                    serializer = KotlinxSerializer()
                }
            }
            var data: List<Post> = client.get<List<Post>> {
                url(NetworkUrl.BASE_URL + NetworkUrl.URL)
            }
            println("response $data")

        }

    }
}