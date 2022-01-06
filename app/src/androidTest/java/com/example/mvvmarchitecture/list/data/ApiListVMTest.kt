package com.example.mvvmarchitecture.list.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mvvmarchitecture.data.remote.Results
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
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    val repo = FakeRepo()

    lateinit var viewModel: ApiListVM2

    @Before
    fun setup() {
        viewModel = ApiListVM2(repo)
    }

    @Test
    fun testData() = runBlockingTest {
        viewModel.getPost()
        var end = viewModel.apiResult.getOrAwaitValue()
        println("end : $end")
        assertEquals(end is Results.Loading, true)
//        viewModel.apiResult.observeForTesting {
//            var res = viewModel.apiResult.value
//            println("received :  " + viewModel.apiResult.value)
//            assertEquals(res is Results.Data<*>, true)
//        }
    }

    @Test
    fun receivedTestDataAfterLoading() = runBlockingTest {
        viewModel.getPost()
        var end = viewModel.apiResult.getOrAwaitValue()
        println("end : $end")
        assertEquals(end is Results.Loading, true)
        delay(10000)
        end = viewModel.apiResult.getOrAwaitValue()
        println("end : $end")
        assertEquals(end is Results.Data<*>, true)
    }

    @Test
    fun testTabNames() = runBlockingTest {
        viewModel.getPost()
        var end = viewModel.tabNames.getOrAwaitValue()
        println("end : $end")
        assertEquals(end.size > 1, true)
    }


}