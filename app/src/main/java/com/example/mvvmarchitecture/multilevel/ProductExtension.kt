package com.example.mvvmarchitecture.multilevel

fun Product.toProductUi() = ProductUi(
    name = name,
    category = category,
    subCategory = subCategory,
    subSubCategory = subSubCategory,
)