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
    private var _networkStatus: MutableStateFlow<Network> = MutableStateFlow(Network.Load)
    private var _products: Flow<List<Product>> =
        _networkStatus.filter { it is Network.Data }.map { (it as Network.Data).data }

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            repo.getList().collect {
                _networkStatus.emit(it)
            }
        }
    }

    val isSortOpen = MutableStateFlow(false)
    private var _categories: Flow<List<String>> =
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
    private var isExisted: (MutableList<String>?, String) -> Boolean =
        { list, selected -> if (list.isNullOrEmpty()) true else list.contains(selected) }
    var productByCategory = combine(
        _networkStatus, /*_products,*/ _selectedCategory, _subCategoryFinal, _subSubCategoryFinal
    ) { network, /*products,*/ selected, subCategories, subSubCategories ->
        if (network is Network.Error) {
            MainScreenAction.Error(network.exception.message.toString())
        } else if (network is Network.Data) {
            var list = network.data
            if (list.isEmpty()) {
                MainScreenAction.Empty("No data found")
            } else {
                MainScreenAction.Data((if (selected == "All") list else list.filter { it.category == selected }).filter {
                    isExisted(subCategories, it.subCategory).and(
                        isExisted(subSubCategories, it.subSubCategory)
                    )
                }.mapIndexed { index, product -> Pair(index, product.toProductUi()) })
            }
        } else
            MainScreenAction.Loading
    }.flowOn(Dispatchers.IO)

    val headerSection = combine(
        _networkStatus,
        _categories,
        _subCategoryFinal,
        _subSubCategoryFinal,
        _selectedCategory
    ) { network, categories, subCategories, subSubCategories, selectedCategory ->
        if (network is Network.Data) {
            if (network.data.isNotEmpty())
                DropDownHeaderAction(
                    selectedCategory = selectedCategory,
                    count = (subCategories.size + subSubCategories.size).toString(),
                    categories = categories.toMutableList().apply { add(0, "All") }
                )
            else DropDownHeaderAction(isShowing = false)
        } else
            DropDownHeaderAction(isShowing = false)
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



