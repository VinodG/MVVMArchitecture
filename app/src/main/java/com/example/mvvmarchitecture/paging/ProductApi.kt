package com.example.mvvmarchitecture.paging

import com.example.mvvmarchitecture.data.remote.Results
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductApi @Inject constructor() {


    fun getProducts(pageSize: Int, pageNumber: Int, startIndex: Int = 0) = flow<Results> {
        emit(Results.Loading(true))
        delay(100)
        println("test: getProduccts")
        emit(Results.Data(((pageSize - 1) * pageSize..pageNumber * pageSize).map {
            Product(id = it, name = "Product name: $it")
        }.toList()))
    }
}