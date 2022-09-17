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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginVM @Inject constructor(private var repo: CommonRepo) : BaseVM() {

    private var arrPost: List<Post> = mutableListOf()
    var arrTemp: List<Post> = mutableListOf()
    private val TAG = "LoginVM"
    val lvPost: MutableLiveData<Results<List<Post>>> = MutableLiveData()

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
            var x = str.split(" ").find { it.firstOrNull().toString() == "#" }
            var startPos = str.indexOf(x.toString())
            var endPos = startPos + x.toString().length
            if (startPos == -1 || endPos == -1) {
                tagText.emit(SpannableString(str))
            } else {
                try {
                    val wordSpan: Spannable = SpannableString(str)
                    tagText.emit(
                        wordSpan.apply {
                            setSpan(
                                ForegroundColorSpan(Color.BLUE),
                                startPos,
                                endPos,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }
                    )
                    x?.let { if (it != "#") selectedTag.emit(it) }

                } catch (e: Exception) {
                    tagText.emit(SpannableString(str))
                }
            }
        }
    }

    private fun getApi(selectedTag: String) = flow {
        emit(Results.Loading(true))
        var list = (0..1000).map { "Item_$it" }.toMutableList()
        val x = if (selectedTag.length > 1) selectedTag.substring(
            1,
            selectedTag.length
        ) else selectedTag
        delay(2000)
        emit(Results.Data(list.filter { it.contains(x, ignoreCase = true) }))
    }

    var selectedTag: MutableStateFlow<String> = MutableStateFlow("")
    val tags: StateFlow<Results<List<String>>> = selectedTag.flatMapConcat { selectedTag ->
        println("Testing: $selectedTag")
        getApi(selectedTag = selectedTag)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        Results.Loading(true)
    )
}
