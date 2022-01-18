package com.example.mvvmarchitecture.list.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.base.Preference
import com.example.mvvmarchitecture.data.Repo
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class ApiListVM @Inject constructor(
//    private var repo: CommonRepo,
//    private var preference: Preference
//) : ViewModel() {
//
//
//    private var _apiResult: MutableLiveData<Results<List<Post>>> = MutableLiveData()
//    val apiResult: LiveData<Results<List<Post>>> = _apiResult
//
//    private var _posts: MutableLiveData<List<Post>> = MutableLiveData(listOf())
//    val tabNames: LiveData<List<String>> = Transformations.map(_posts) {
//        it?.let { data ->
//            mutableListOf<String>().apply {
//                add("All")
//                addAll(data.map {
//                    it.title ?: ""
//                }.filter { it.isNotEmpty() }.distinct())
//            }
//        } ?: listOf("All")
//    }
//    var _selectedTab: MutableLiveData<String> = MutableLiveData("All")
//
//    var filteredData: LiveData<Results<List<Post>>> = Transformations.map(_selectedTab) { filter ->
//        println("$filter  -> ${_posts.value}")
//        Results.Data(
//            _posts.value?.let { list ->
//                filter?.let { str ->
//                    if (str != "All") {
//                        list.filter { it.title?.equals(str) ?: false }
//                    } else
//                        list
//                } ?: list
//            } ?: listOf<Post>())
//    }
//
//
//    fun getPost() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                _apiResult.postValue(Results.Loading(true))
//                repo.get().apply {
//                    _posts.postValue(this.toMutableList())
//                    _selectedTab.postValue("All")
//                }
//            } catch (e: Exception) {
//                _apiResult.postValue(Results.Error(e))
//            }
//            _apiResult.postValue(Results.Loading(false))
//        }
//    }
//
//
//    var lastValue = 0
//    fun increment() {
//        viewModelScope.launch {
//            var inc = lastValue + 1
//            println("token-resume-incremented ${inc}")
//            preference.setToken(inc)
//        }
//    }
//
//    fun getCounter() {
//        viewModelScope.launch {
//            preference.getToken {
//                it.let {
//                    lastValue = it
//                    println("token -collect $it")
//                }
//            }
//        }
//    }
//
//    fun filter(it: String) {
//        _selectedTab.postValue(it)
//    }
//
//
//}
@HiltViewModel
class ApiListVM @Inject constructor(
    private var repo: Repo,
    private var preference: Preference
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
                    delay(3000)
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

    fun filter(str: String) {
        _apiResult.postValue(Results.Loading(true))
        if (str.isEmpty() || str == "All") {
            _apiResult.postValue(Results.Data(_posts))
        } else {
            _apiResult.postValue(Results.Data(_posts.filter { it.title?.equals(str) ?: false }))
        }
    }

    var lastValue = 0
    fun increment() {
        viewModelScope.launch {
            var inc = lastValue + 1
            println("token-resume-incremented ${inc}")
            preference.setToken(inc)
        }
    }

    fun getCounter() {
        viewModelScope.launch {
            preference.getToken {
                it.let {
                    lastValue = it
                    println("token -collect $it")
                }
            }
        }
    }

}
