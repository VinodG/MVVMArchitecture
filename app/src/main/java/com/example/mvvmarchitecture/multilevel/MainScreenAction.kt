package com.example.mvvmarchitecture.multilevel

sealed class MainScreenAction() {
    object Loading : MainScreenAction()
    data class Error(var message: String) : MainScreenAction()
    data class Empty(var message: String) : MainScreenAction()
    data class Data(var products: List<Pair<Int, ProductUi>>) : MainScreenAction()
}