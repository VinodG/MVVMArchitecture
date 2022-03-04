package com.example.mvvmarchitecture.multilevel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductList(list: List<Product>, onClick: (Product) -> Unit) {
    LazyColumn {
        items(list) { product ->
            Row(
                Modifier
                    .padding(4.dp)
                    .clickable { onClick(product) }) {
                Text(text = "${product.name} \n${product.category} \n${product.subCategory}\n${product.subSubCategory}")
            }
        }
    }
}