package com.example.mvvmarchitecture.multipart_image

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class PolymorphismViewModel @Inject constructor(
    val repo: CommonRepo,
    @ApplicationContext val context: Context
) : ViewModel() {
    private val TAG = "PolymorphismViewModel"

    val state: MutableStateFlow<List<Post>> = MutableStateFlow(listOf())


    init {
        getList()
    }

    fun getList() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getResponseBody().collectLatest { x ->
                when (x) {
                    is MyResult.Success<ResponseBody?> -> {
                        Log.e(TAG, "getList: success "+x.data?.string()+" end")
                        state.emit(listOf())
                    }

                    is MyResult.Error -> Log.e(TAG, "getList: error ")
                    else -> Log.e(TAG, "getList: loading ")
                }
            }
//            repo.getX().collectLatest { x ->
//                when (x) {
//                    is MyResult.Success<List<Post>?> -> {
//                        Log.e(TAG, "getList: success ")
//                        state.emit(x.data.orEmpty())
//                    }
//
//                    is MyResult.Error -> Log.e(TAG, "getList: error ")
//                    else -> Log.e(TAG, "getList: loading ")
//                }
//            }
        }
    }

    fun uploadImage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //println(repo.getJson().string())
                val file = File(context.cacheDir, "example.png")
                val fbody: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                val part =
                    MultipartBody.Part.createFormData("file_name", file.name + "asdfas", fbody)
                val description =
                    "vinod Kumar at time".toRequestBody("text/plain".toMediaTypeOrNull())
                println(repo.postImage(part, description).string())
            } catch (e: Exception) {

            }
        }
    }

    fun getPolyList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getPoly()
                if (response.isSuccessful) {
                    var data = response.body()
                    Log.e(TAG, "getPolyList: $data")
                }
                Log.e(TAG, "getPolyList: ${response.body()}")
            } catch (e: Exception) {
                Log.e(TAG, "getPolyList: ${e.message}")


                e.printStackTrace()
            }
        }
    }
}