package com.example.mvvmarchitecture.temp

import androidx.lifecycle.*
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TempVM @Inject constructor(
    var repo: TempRepo,
    var resManager: TempResManager,
    var dispatcher: CoroutineDispatcher,
    var currentDate: Date
) : ViewModel() {

    private var _apiResult: MutableLiveData<Results<List<Post>>> = MutableLiveData()
    var apiResult: LiveData<Results<List<Post>>> = _apiResult
    private var _posts: List<Post> = mutableListOf()
    private var _tabNames: MutableLiveData<List<String>> = MutableLiveData()

    val tabNames: LiveData<List<String>> = _apiResult.switchMap { result ->
        liveData(dispatcher) {
            emit(when (result) {
                is Results.Data -> result.data.map { it.title ?: "" }
                    .filter { it.isNotEmpty() }
                    .distinct().toMutableList().apply {
                        add(0, "All")
                    }
                else -> listOf("All")
            })

        }
    }

    fun getPost() {
        viewModelScope.launch(dispatcher) {
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

    fun getString() =
        resManager.getString(R.string.app_name)

    val delayLiveData: MutableLiveData<Boolean> = MutableLiveData()
    fun testDelay(delay: Long) =
        viewModelScope.launch(dispatcher) {
            delayLiveData.postValue(false)
            delay(delay)
            delayLiveData.postValue(true)
        }

    fun addDays(days: Int): Date {
        val cal = Calendar.getInstance().apply { time = currentDate }
        cal.add(Calendar.DAY_OF_MONTH, days)
        return cal.time
    }

    fun getDate(date: Int, month: Int, year: Int, format: String): String {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = date
        cal[Calendar.MONTH] = month
        cal[Calendar.YEAR] = year
        return SimpleDateFormat(format).format(cal.time)
    }
}
