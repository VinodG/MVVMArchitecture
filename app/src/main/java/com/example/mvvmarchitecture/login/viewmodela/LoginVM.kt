package com.example.mvvmarchitecture.login.viewmodela

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.base.BaseVM
import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginVM @Inject constructor(private var repo: CommonRepo) : BaseVM() {

    private var arrPost: List<Post> = mutableListOf()
    var arrTemp: List<Post> = mutableListOf()
    private val TAG = "LoginVM"
    val lvPost: MutableLiveData<Results<List<Post>>> = MutableLiveData()
    val tags: MutableStateFlow<Results<List<String>>> =
        MutableStateFlow(Results.Loading(isLoading = true))
    val tagText: MutableStateFlow<Spannable> =
        MutableStateFlow(SpannableString(""))

    fun getPost() {
        viewModelScope.launch(Dispatchers.IO) {
            showLoader()
            try {
                arrPost = repo.get()
                arrTemp = arrPost
                lvPost.postValue(Results.Data(arrPost))
            } catch (e: Exception) {
                lvPost.postValue(Results.Error(e))
            }
            hideLoader()
            Log.e(TAG, "getPost:  ")
        }
    }

    fun filter(str: Spannable) {
        viewModelScope.launch {
            // "I know just how to #whisper, And I #know just how to cry,I know just where to find the answers"
            var x = str.split(" ").find { it.firstOrNull().toString() == "#" }
            var startPos = str.indexOf(x.toString())
            var endPos = startPos + x.toString().length - 1
            try {
                val wordtoSpan: Spannable =
                    SpannableString(str)
                tagText.emit(
                    wordtoSpan.apply {
                        setSpan(
                            ForegroundColorSpan(Color.BLUE),
                            startPos,
                            endPos,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getTags() {
        viewModelScope.launch(Dispatchers.IO) {
            tags.emit(Results.Loading(true))
            var list = (0..1000).map { "Item : $it" }.toMutableList()
            tags.emit(Results.Data(list))
        }
    }


}