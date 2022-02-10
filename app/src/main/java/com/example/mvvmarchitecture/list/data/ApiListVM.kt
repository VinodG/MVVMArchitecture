package com.example.mvvmarchitecture.list.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.data.Repo
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiListVM @Inject constructor(
    private var repo: Repo,
    private var dispatcherIO: CoroutineDispatcher
) : ViewModel() {

    private var TAB_ALL: String = "All"

    private var _api: MutableStateFlow<Results> = MutableStateFlow(Results.Loading(true))
    private var _posts: MutableStateFlow<List<Post>?> = MutableStateFlow(null)
    private var _selectedTab: MutableSharedFlow<String> = MutableStateFlow(TAB_ALL)
    private var _filteredData = _selectedTab.combine(_posts) { tabName, posts ->
        posts?.filter { it.title == tabName || tabName == TAB_ALL } ?: listOf()
    }
    private var _tabNames = _posts.map { posts ->
        posts?.filter { !it.title.isNullOrBlank() }?.map { it.title ?: "" }?.toMutableList()
            ?.apply {
                add(0, TAB_ALL)
            } ?: listOf(TAB_ALL)
    }.flowOn(dispatcherIO)


    val uiData: StateFlow<Results> = _api.combine(_filteredData) { api, posts ->
        when {
            api == null -> api
            posts.isEmpty() -> Results.Empty("Data not found")
            else -> Results.Data(posts)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Results.Loading(true))

    val tabNames: StateFlow<List<String>> = _tabNames.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        listOf()
    )

    fun getPost() {
        viewModelScope.launch(dispatcherIO) {
            try {
                _api.emit(Results.Loading(true))
                _posts.emit(repo.getApi())
            } catch (e: Exception) {
                _api.emit(Results.Error(e))
            }
            _api.emit(Results.Loading(false))
        }
    }

    fun onSelectedTab(tabName: String) = viewModelScope.launch {
        _selectedTab.emit(tabName)
    }

    fun removeItem(id: String) = viewModelScope.launch(dispatcherIO) {
        _posts.emit(_posts.value?.filter { it.id != id } ?: listOf())
    }

}
