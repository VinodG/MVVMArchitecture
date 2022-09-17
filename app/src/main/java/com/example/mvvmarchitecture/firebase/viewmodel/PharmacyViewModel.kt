package com.example.mvvmarchitecture.firebase.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.firebase.models.ApiState
import com.example.mvvmarchitecture.firebase.models.Pharmacy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PharmacyViewModel @Inject constructor(var repository: PharmacyRepository) : ViewModel() {

    val pharmacies =
        repository.getPharmacies().mapNotNull { if (it is ApiState.Success) it.data else null }


    fun addPharmacy() {
        viewModelScope.launch {
            var index = Random.nextInt().toString()
            val pharmacy = Pharmacy(index, "name $index", "address $index")
            repository.addPharmacy(pharmacy = pharmacy).collect {
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


}