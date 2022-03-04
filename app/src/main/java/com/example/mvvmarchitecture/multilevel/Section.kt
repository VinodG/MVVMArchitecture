package com.example.mvvmarchitecture.multilevel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mvvmarchitecture.R

@Composable
fun Section(list: List<Pair<Boolean, String>>, onClick: (String) -> Unit) {
    LazyColumn {
        items(list) { product ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable { onClick(product.second) }) {
                Text(text = product.second)
                if (product.first)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
            }

        }
    }

}