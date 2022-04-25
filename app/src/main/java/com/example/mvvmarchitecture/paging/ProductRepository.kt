package com.example.mvvmarchitecture.paging

import com.example.mvvmarchitecture.data.remote.Results
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProductRepository @Inject constructor(private var apiProduct: ProductApi) {

    private var _products: MutableStateFlow<List<Product>> = MutableStateFlow<List<Product>>(
        listOf()
    )
    var products: Flow<List<Product>> = _products
    var prod = listOf<Product>()
    var pageNumber: Int = 1

    suspend fun getProducts(pageSize: Int, startIndex: Int = 0) {
        apiProduct.getProducts(pageNumber = pageNumber, pageSize = pageSize).collect {
            if (it is Results.Data<*>) {
                var products = (it.data as List<Product>)
                var x = prod.toMutableSet().apply {
                    addAll(products)
                }.toMutableSet().toMutableList()
                prod = x
                println("after test: ${x.size}   $pageNumber")
                this.pageNumber++
                _products.emit(x)
            }
        }

    }
}