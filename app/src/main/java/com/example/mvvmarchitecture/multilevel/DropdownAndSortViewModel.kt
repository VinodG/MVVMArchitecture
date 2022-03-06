package com.example.mvvmarchitecture.multilevel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmarchitecture.data.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DropdownAndSortViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {
    private var _products: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())

    //    var _products: Flow<List<Product>> =
//        repo.getList().filter { it is Network.Data }.map { (it as Network.Data).data }
//            .flowOn(Dispatchers.IO)
    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getList().filter { it is Network.Data }
                .map { (it as Network.Data).data }.collect {
                    _products.emit(it)
                }
        }
    }

    val isSortOpen = MutableStateFlow(false)
    var _categories: Flow<List<String>> =
        _products.map { it.map { it.category }.distinct() }.flowOn(Dispatchers.IO)
    private var _selectedCategory: MutableStateFlow<String> = MutableStateFlow("All")
    private var _subCategoryTick: MutableStateFlow<MutableList<String>> =
        MutableStateFlow(mutableListOf())
    private var _subCategoryFinal: MutableStateFlow<MutableList<String>> =
        MutableStateFlow(mutableListOf())
    var _subCategories: Flow<List<Pair<Boolean, String>>> =
        combine(_subCategoryTick, _products) { subcategories, products ->
            products.map { Pair(subcategories.contains(it.subCategory), it.subCategory) }.distinct()
        }
    private var _subSubCategoryTick: MutableStateFlow<MutableList<String>> =
        MutableStateFlow(mutableListOf())
    private var _subSubCategoryFinal: MutableStateFlow<MutableList<String>> =
        MutableStateFlow(mutableListOf())
    var _subSubCategories: Flow<List<Pair<Boolean, String>>> =
        combine(_subSubCategoryTick, _products) { subcategories, products ->
            products.map { Pair(subcategories.contains(it.subSubCategory), it.subSubCategory) }
                .distinct()
        }
    var filterCount =
        combine(_subCategoryFinal, _subSubCategoryFinal) { subCategory, subSubCategory ->
            subCategory.size + subSubCategory.size
        }
    var isExisted: (MutableList<String>?, String) -> Boolean =
        { list, selected -> if (list.isNullOrEmpty()) true else list.contains(selected) }
    var _productByCategory = combine(
        _products, _selectedCategory, _subCategoryFinal, _subSubCategoryFinal
    ) { products, selected, subCategories, subSubCategories ->
        (if (selected == "All")
            products
        else
            products.filter { it.category == selected }).filter {
            isExisted(subCategories, it.subCategory).and(
                isExisted(subSubCategories, it.subSubCategory)
            )
        }
    }

    fun selectedSubCategory(subCategory: String) = viewModelScope.launch {
        val list = _subCategoryTick.value.toMutableList()
        if (list.contains(subCategory))
            list.remove(subCategory)
        else
            list.add(subCategory)
        _subCategoryTick.emit(list)
    }

    fun selectedSubSubCategory(subSubCategory: String) = viewModelScope.launch {
        val list = _subSubCategoryTick.value.toMutableList()
        if (list.contains(subSubCategory))
            list.remove(subSubCategory)
        else
            list.add(subSubCategory)
        _subSubCategoryTick.emit(list)
    }

    fun selectedCategory(category: String) = viewModelScope.launch {
        _selectedCategory.emit(category)
    }

    fun toggleSortSection() = viewModelScope.launch {
        if (!isSortOpen.value) {
            _subCategoryTick.emit(_subCategoryFinal.value.toMutableList())
            _subSubCategoryTick.emit(_subSubCategoryFinal.value.toMutableList())
        }
        isSortOpen.emit(!isSortOpen.value)
    }

    fun applyFilter() = viewModelScope.launch {
        _subCategoryFinal.emit(_subCategoryTick.value.toMutableList())
        _subSubCategoryFinal.emit(_subSubCategoryTick.value.toMutableList())
        toggleSortSection()
    }

}



