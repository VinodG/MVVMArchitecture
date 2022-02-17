package com.example.mvvmarchitecture.list.data

import androidx.lifecycle.*
import com.example.mvvmarchitecture.base.Preference
import com.example.mvvmarchitecture.data.Repo
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiListVM @Inject constructor(
    private var repo: Repo,
    private var preference: Preference
) : ViewModel() {


    private var _apiResult: MutableLiveData<Results<List<Post>>> = MutableLiveData()
    val apiResult: LiveData<Results<List<Post>>>
        get() = _apiResult

    private var _posts: List<Post> = mutableListOf()

    val tabNames: LiveData<List<String>> = _apiResult.switchMap { result ->
        liveData {
            emit(
                if (result is Results.Data) {
                    result.data.map { it.title ?: "" }
                        .filter { it.isNotEmpty() }
                        .distinct().toMutableList().apply {
                            add(0, "ALL")
                        }
                } else
                    listOf("ALL")
            )
        }
    }


    private var selectedTab = "All"


    fun getPost() {
        viewModelScope.launch/*(Dispatchers.IO) */{
            try {
                _apiResult.postValue(Results.Loading(true))
                repo.getApi().apply {
//                    delay(3000)
                    _posts = this.toMutableList()
                    _apiResult.postValue(Results.Data(_posts))
                }
            } catch (e: Exception) {
                _apiResult.postValue(Results.Error(e))
            }
        }
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
