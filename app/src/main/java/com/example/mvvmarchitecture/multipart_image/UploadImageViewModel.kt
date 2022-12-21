package com.example.mvvmarchitecture.multipart_image

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.data.CommonRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class UploadImageViewModel @Inject constructor(
    val repo: CommonRepo,
    @ApplicationContext val context: Context
) : ViewModel() {

    fun getList() {
        viewModelScope.launch(Dispatchers.IO) {
            println(repo.getApi().toString())
        }
    }

    fun uploadImage() {
        viewModelScope.launch(Dispatchers.IO) {
            //println(repo.getJson().string())
            val file = File(context.cacheDir, "example.png")
            val fbody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file_name", file.name+"asdfas", fbody)
            val description  = "vinod Kumar at time".toRequestBody("text/plain".toMediaTypeOrNull())
            println(repo.postImage(part,description).string())
        }
    }
}