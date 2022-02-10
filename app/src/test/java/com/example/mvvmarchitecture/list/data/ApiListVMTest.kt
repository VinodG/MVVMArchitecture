package com.example.mvvmarchitecture.list.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmarchitecture.CoroutineTestRule
import com.example.mvvmarchitecture.base.Preference
import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class ApiListVM2Test {

    var repo = mock(CommonRepo::class.java)
    lateinit var vm: ApiListVM

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    //    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
    val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        runBlocking {
            `when`(repo.getApi()).thenReturn(
                listOf(
                    Post(
                        id = "1",
                        userId = "1",
                        title = "title",
                        body = "body"
                    )
                )
            )
            vm = ApiListVM(repo, testDispatcher)
        }
    }

    @Test
    fun `get() return list of Posts`() = runBlocking {
        var p = repo.getApi()
        Assert.assertEquals(p, listOf(Post(userId = null)))
    }

    @Test
    fun `given api data tab names are received properly`() = testDispatcher.runBlockingTest {
        vm.getPost()
        var x = vm.tabNames.first()
        println(x)
        assertEquals(x.size, 2)
    }

    @Test
    fun `given api data properly`() = testDispatcher.runBlockingTest {
        vm.getPost()
        var x = vm.uiData.first()
        println(x)
        assertEquals((x as Results.Data<List<Post>>).data.size, 2)
    }
}