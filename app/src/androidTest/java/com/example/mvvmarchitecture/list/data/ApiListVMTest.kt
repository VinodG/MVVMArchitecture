package com.example.mvvmarchitecture.list.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.list.ui.ApiListActivity
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ApiListVMTest : TestCase() {

    @get:Rule
    val composeRule = createAndroidComposeRule<ApiListActivity>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val repo = FakeRepo()

    lateinit var viewModel: ApiListVM2

//    var x = mock(CommonRepo::class.java)

    @Before
    fun setup() {
        viewModel = ApiListVM2(repo)
    }

    @Test
    fun getxString() {
        println("appname : ${composeRule.activity.getString(R.string.app_name)}")
        assert(true)
    }

    @Test
    fun testData() = runBlockingTest {
        viewModel.getPost()
        val response = viewModel.apiResult.getOrAwaitValue()
        println("end : $response")
        assertEquals(response is Results.Loading, true)
//        viewModel.apiResult.observeForTesting {
//            var res = viewModel.apiResult.value
//            println("received :  " + viewModel.apiResult.value)
//            assertEquals(res is Results.Data<*>, true)
//        }
    }

    @Test
    fun receivedTestDataAfterLoading() = runBlockingTest {
        viewModel.getPost()
        var response = viewModel.apiResult.getOrAwaitValue()
        println("end : $response")
        assertEquals(response is Results.Loading, true)
        delay(10000)
        response = viewModel.apiResult.getOrAwaitValue()
        println("end : $response")
        assertEquals(response is Results.Data<*>, true)
    }

    @Test
    fun testTabNames() = runBlockingTest {
        viewModel.getPost()
        var response = viewModel.tabNames.getOrAwaitValue()
        println("end : $response")
        assertEquals(response.size > 1, true)
    }

}