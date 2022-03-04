package com.example.mvvmarchitecture.multilevel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mvvmarchitecture.theme.MVVMArchitectureTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DropdownAndSortActivity : ComponentActivity() {
    val vm: DropdownAndSortViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MVVMArchitectureTheme {
                Column {
                    val isSortOpen by vm.isSortOpen.collectAsState(false)
                    if (isSortOpen) {
                        SortSection(vm)
                    } else {
                        MainScreen(vm)
                    }
                }
            }
        }
    }

    @Composable
    fun SortSection(vm: DropdownAndSortViewModel) {
        val subCategories = vm._subCategories.collectAsState(initial = listOf())
        val subSubCategories = vm._subSubCategories.collectAsState(initial = listOf())
        Row {
            Button(onClick = vm::toggleSortSection) {
                Text(text = "Close")
            }
        }
        Text(text = "Sub Category")
        Section(subCategories.value, this.vm::selectedSubCategory)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "SubSub Category")
        Section(subSubCategories.value, this.vm::selectedSubSubCategory)
        Button(onClick = vm::applyFilter) {
            Text(text = "Apply")
        }

    }

    @Composable
    fun MainScreen(vm: DropdownAndSortViewModel) {
        var products = vm._productByCategory.collectAsState(initial = listOf())
        var categories = vm._categories.collectAsState(initial = listOf())

        Button(onClick = { vm.toggleSortSection() }) {
            Text(text = "Open")
        }
        DropDownSection(categories.value, vm::selectedCategory)
        ProductList(products.value) {}

    }
}
