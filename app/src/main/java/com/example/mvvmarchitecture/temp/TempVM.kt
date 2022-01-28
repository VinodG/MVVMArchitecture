package com.example.mvvmarchitecture.temp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TempVM @Inject constructor(
    var repo: TempRepo,
    var resManager: TempResManager
) : ViewModel() {


    private var _apiResult: MutableLiveData<Results<List<Post>>> = MutableLiveData()
    val apiResult: LiveData<Results<List<Post>>>
        get() = _apiResult

    private var _posts: List<Post> = mutableListOf()
    private var _tabNames: MutableLiveData<List<String>> = MutableLiveData()
    val tabNames: LiveData<List<String>>
        get() = _tabNames

    private var selectedTab = "All"


    fun getPost() {
        viewModelScope.launch/*(Dispatchers.IO) */{
            try {
                _apiResult.postValue(Results.Loading(true))
                repo.getApi().apply {
//                    delay(3000)
                    _posts = this.toMutableList()
                    _apiResult.postValue(Results.Data(_posts))
                    getTabNames(_posts)
                }
            } catch (e: Exception) {
                _apiResult.postValue(Results.Error(e))
            }
        }
    }

    private fun getTabNames(data: List<Post>) {
        var list = mutableListOf<String>().apply {
            add("All")
        }
        list.addAll(data.map { it.title ?: "" }.filter { it.isNotEmpty() }.distinct())
        _tabNames.postValue(list.toList())
    }

    fun getString() =
        resManager.getString(R.string.app_name)

}
