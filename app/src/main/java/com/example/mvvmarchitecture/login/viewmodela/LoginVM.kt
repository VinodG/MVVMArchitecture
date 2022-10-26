package com.example.mvvmarchitecture.login.viewmodela

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.base.BaseVM
import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(private var repo: CommonRepo) : BaseVM() {

    private var arrPost: List<Post> = mutableListOf()
    var arrTemp: List<Post> = mutableListOf()
    val lvPost: MutableLiveData<Results<List<Post>>> = MutableLiveData()

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
        }
    }

    fun filter(str: String) {
        viewModelScope.launch {
            arrTemp = if (str.isEmpty()) arrPost else arrPost.filter {
                (it.body?.contains(str, ignoreCase = true) ?: false)
                        || (it.title?.contains(str, ignoreCase = true) ?: false)
                        || (it.id?.contains(str, ignoreCase = true) ?: false)
            }
            lvPost.postValue(Results.Data(arrTemp))
        }
    }


}