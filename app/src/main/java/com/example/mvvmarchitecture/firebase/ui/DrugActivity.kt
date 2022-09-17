package com.example.mvvmarchitecture.firebase.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mvvmarchitecture.firebase.models.Drug
import com.example.mvvmarchitecture.firebase.ui.ui.theme.MVVMArchitectureTheme
import com.example.mvvmarchitecture.firebase.viewmodel.DrugViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrugActivity : ComponentActivity() {
    val viewModel: DrugViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MVVMArchitectureTheme {
                val drugs by viewModel.drugs.collectAsState(initial = listOf())
                DrugScreen(drugs, viewModel::onQuery)
            }
        }
    }

    @Composable
    fun DrugScreen(drugs: List<Drug>, onClick: () -> Unit) {
        Column {
            Button(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
                Text(text = "Add Drug")
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = "total size: ${drugs.size}"
                    )
                }
                itemsIndexed(drugs, key = { _, item -> item.id }) { _, drug ->
                    Card(Modifier.padding(8.dp)) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        ) {
                            Text(text = drug.name)
                            Text(text = drug.dosage)
                        }
                    }

                }
            }
        }
    }
}

