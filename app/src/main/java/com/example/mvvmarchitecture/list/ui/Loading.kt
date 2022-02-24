package com.example.mvvmarchitecture.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Loading() {
    Text(
        text = "Loading...",
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Green)
    )
}