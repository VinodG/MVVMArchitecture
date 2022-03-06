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
import com.example.mvvmarchitecture.multilevel.MainScreenAction.*
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
        val products by vm.productByCategory.collectAsState(initial = Loading)
        val header by vm.headerSection.collectAsState(initial = DropDownHeaderAction(isShowing = false))
        if (header.isShowing) {
            Text(text = header.selectedCategory)
            DropDownSection(header.categories, header.count, vm::selectedCategory, vm::toggleSortSection)
        }
        when (products) {
            is Loading -> Text("Loading")
            is Empty -> Text((products as Empty).message)
            is Error -> Text((products as Error).message)
            is Data -> ProductList((products as Data).products) {}
        }
    }
}
