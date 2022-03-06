package com.example.mvvmarchitecture.multilevel

data class DropDownHeaderAction(
    var isShowing: Boolean = true,
    var selectedCategory: String = "",
    var count: String = "0",
    val categories: List<String> = listOf()
)