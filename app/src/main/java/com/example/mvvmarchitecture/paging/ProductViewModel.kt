package com.example.mvvmarchitecture.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private var repo: ProductRepository) : ViewModel() {


    var products: Flow<List<Product>> = repo.products

    init {
        getProductsApi()
    }

    fun getProductsApi() = viewModelScope.launch(Dispatchers.IO) {
        repo.getProducts(pageSize = PAGE_SIZE)
    }


    companion object {
        const val PAGE_SIZE = 10
    }


}