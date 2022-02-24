package com.example.mvvmarchitecture.list.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class ApiListActivityTest {
    @get:Rule
    var rule = createComposeRule()

    @Test
    fun list_is_showing() {
        rule.setContent {
            Loading()
        }
        rule.onNodeWithText("Loading...").assertExists()
    }
}