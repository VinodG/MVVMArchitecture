package com.example.mvvmarchitecture.list.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.list.data.ApiListVM
import com.example.mvvmarchitecture.theme.MVVMArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApiListActivity : ComponentActivity() {
    val viewModel: ApiListVM by viewModels()
    private val TAG = "ApiListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPost()
        setContent {
            val tabNames by viewModel.tabNames.collectAsState()
            val uiState = viewModel.uiData.collectAsState()
            Log.e(TAG, "onCreate: ")
            MVVMArchitectureTheme {
                Column {
                    TopSection(tabNames) {
                        viewModel.onSelectedTab(it)
                    }
                    uiState.let {
                        ApiListScreen(uiState = it.value)
                    }
                }
            }
        }
    }


    @Composable
    fun TopSection(tabNames: List<String>?, function: (String) -> Unit) {
        tabNames?.let {
            Spacer(modifier = Modifier.height(24.dp))
            LazyRow {
                items(it.size) { pos ->
                    Text(
                        text = it[pos],
                        modifier = Modifier
                            .clickable { function(it[pos]) }
                            .padding(5.dp)
                            .width(50.dp)
                            .background(Color.Red)
                            .padding(5.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }

    @Composable
    fun ApiListScreen(uiState: Results) {
//        Log.e(TAG, "ApiListScreen: $uiState")
        when (uiState) {
            is Results.Loading -> {
                if (uiState.isLoading)
                    Loading()
            }
            is Results.Data<*> -> {
                ListScreen(uiState.data as List<Post>)
            }
            is Results.Empty -> {
                DataNotFound(uiState.data)
            }
            else -> {
                ErrorScreen(uiState as Results.Error)
            }
        }

    }

    private @Composable
    fun DataNotFound(data: String) {
        Text(
            text = data,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.Cyan),
            textAlign = TextAlign.Center,
            maxLines = 1
        )

    }

    @Composable
    fun Loading() {
        Text(
            text = "Loading",
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.Green),
            textAlign = TextAlign.Center
        )
    }

    @Composable
    fun ListScreen(list: List<Post>) {
        LazyColumn {
            itemsIndexed(list) { pos, data ->
                Text(text = "Title : ${data.title} \nBody : ${data.body}",
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            viewModel.removeItem(data.id ?: "")
                        })
                Divider(Modifier.height(2.dp))
            }
        }
    }

    @Composable
    fun ErrorScreen(error: Results.Error) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(text = error.error.message ?: "Error is occured")
        }
    }
}