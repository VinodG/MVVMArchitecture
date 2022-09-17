package com.example.mvvmarchitecture.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.firebase.models.ApiState
import com.example.mvvmarchitecture.firebase.models.Drug
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class DrugViewModel @Inject constructor(var repository: DrugRepository) : ViewModel() {

    val drugs =
        repository.getDrugs().mapNotNull { if (it is ApiState.Success) it.data else null }


    fun addDrug() {
        viewModelScope.launch {
            var index = Random.nextInt().toString()
            val drug = Drug(index, "name $index", "address $index")
            repository.addDrug(  drug).collect {
                when (it) {
                    is ApiState.Success -> {
                        println("result: ${it.data}")
                    }
                    is ApiState.Loader -> {

                    }
                    is ApiState.Error -> {
                    }
                }
            }
        }
    }

    fun onQuery(){
        viewModelScope.launch {
            repository.onQuery("ame 141").collect {
                when(it){
                    is ApiState.Success -> {
                        println("TESTING: ${it.data}")
                    }
                }
            }
        }
    }


}