package com.example.mvvmarchitecture.list.ui

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mvvmarchitecture.base.AppViewModel
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.list.data.ApiListVM
import com.example.mvvmarchitecture.login.ui.LoginActivity
import com.example.mvvmarchitecture.theme.MVVMArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ApiListActivity : ComponentActivity() {

    val viewModel: ApiListVM by viewModels()

    @Inject
    lateinit var appViewModel: AppViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel.x = 2
        setContent {
            viewModel.getPost()
            viewModel.getCounter()

            MVVMArchitectureTheme {
                println("recomposition")
                val uiState by viewModel.apiResult.observeAsState()
                val tabNames by viewModel.tabNames.observeAsState()
                Column {
                    TopSection(tabNames) {
                        viewModel.filter(it)
                    }
                    uiState?.let {
                        ApiListScreen(uiState = it)
                    }
                }
            }
        }
        startActivity(Intent(this, LoginActivity::class.java))
    }


    override fun onResume() {
        super.onResume()
        viewModel.increment()
        println("result: ${appViewModel.x}")
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
                            .padding(5.dp)
                            .background(Color.Red)
                            .padding(5.dp)
                            .clickable { function(it[pos]) }

                    )
                }
            }
        }
    }

    @Composable
    fun ApiListScreen(uiState: Results<List<Post>>) {
        when (uiState) {
            is Results.Loading -> {
                Loading()
            }
            is Results.Data -> {
                ListScreen(uiState.data)
            }
            else -> {
                ErrorScreen(uiState as Results.Error)
            }
        }

    }

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

    @Composable
    fun ListScreen(list: List<Post>) {
        LazyColumn {
            itemsIndexed(list) { pos, data ->
                Text(text = "Title : ${data.title} \nBody : ${data.body}",
                    modifier = Modifier.clickable {
                        startActivity(
                            Intent(
                                this@ApiListActivity,
                                LoginActivity::class.java
                            )
                        )
                    })
                Divider(Modifier.height(2.dp))
            }
        }
    }

    @Composable
    fun ErrorScreen(error: Results.Error<List<Post>>) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(text = error.error.message ?: "Error is occured")
        }
    }
}