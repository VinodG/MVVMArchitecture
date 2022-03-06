package com.example.mvvmarchitecture.multilevel

sealed class Events {
    object OnOpenDropDown : Events()
    data class OnOptionSelect(var category: String) : Events()
    data class OnItemClick(var product: Product) : Events()
    object OnCloseFilter : Events()
    object ApplyFilter : Events()
}