package com.example.mvvmarchitecture.list.ui

//import com.example.mvvmarchitecture.login.ui.LoginActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.list.data.ApiListVM
import com.example.mvvmarchitecture.temp.TempVM
import com.example.mvvmarchitecture.theme.MVVMArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApiListActivity : ComponentActivity() {

    val viewModel: ApiListVM by viewModels()
    val test: TempVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.getPost()
            viewModel.getCounter()
            MVVMArchitectureTheme {
                println("recomposition")
                val uiState by viewModel.apiResult.observeAsState()
                val tabNames by viewModel.tabNames.observeAsState()
//                val data by viewModel.filteredData.observeAsState()
                Column {
                    TopSection(tabNames) {
                        viewModel.filter(it)
                    }
                    uiState?.let {
                        ApiListScreen(uiState = it)
                    }
                    /* data?.let {
                         (it as Results.Data<List<Post>>)
                         if (!it.data.isEmpty())
                             ApiListScreen(uiState = it)

                     }*/
                }
            }
        }
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

    @Composable
    fun ApiListScreen(uiState: Results<List<Post>>) {
        when (uiState) {
            is Results.Loading -> {
                if (uiState.isLoading)
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



}