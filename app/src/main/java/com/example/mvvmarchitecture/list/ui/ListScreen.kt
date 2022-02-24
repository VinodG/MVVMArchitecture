package com.example.mvvmarchitecture.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.mvvmarchitecture.data.models.Post

@Composable
fun ListScreen(list: List<Post>) {
    LazyColumn(modifier = Modifier.testTag("list_tag")) {
        itemsIndexed(list) { pos, data ->
            Text(text = "Title : ${data.title} \nBody : ${data.body}",
                modifier = Modifier.clickable {
                })
            Divider(Modifier.height(2.dp))
        }
    }
}