package com.example.mvvmarchitecture.list.ui

import android.content.Context
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
import com.example.mvvmarchitecture.base.Preference
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.di.AppModule
import com.example.mvvmarchitecture.list.data.ApiListVM
import com.example.mvvmarchitecture.login.ui.LoginActivity
import com.example.mvvmarchitecture.theme.MVVMArchitectureTheme
import dagger.hilt.EntryPoints
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent

import dagger.hilt.InstallIn

import dagger.hilt.EntryPoint


@AndroidEntryPoint
class ApiListActivity : ComponentActivity() {

    private val viewModel: ApiListVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.getPost()
            viewModel.getCounter()
            MVVMArchitectureTheme {
                println("recomposition " + TempInjection(applicationContext).getApi())
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
    }

    class TempInjection(var applicationContext: Context) {
        fun getPreference(): Preference {
            return EntryPoints.get(applicationContext, MyClassInterface::class.java).foo
        }

        fun getApi(): Api {
            return EntryPoints.get(applicationContext, MyClassInterface::class.java).api
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface MyClassInterface {
        val foo: Preference
        val api: Api
    }

    override fun onResume() {
        super.onResume()
        viewModel.increment()
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
//    class Temp(){
//        fun getPreference() : Preference
//    }

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
            itemsIndexed(list) { _, data ->
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