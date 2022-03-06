package com.example.mvvmarchitecture.multilevel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job


@Composable
fun DropDownSection(list: List<String>, count: String, onClick: (String) -> Job) {
    Text(text = "($count)")
    LazyRow {
        items(list) { category ->
            Row(
                Modifier
                    .padding(4.dp)
                    .clickable { onClick(category) }) {
                Text(text = category)
            }
        }
    }

}