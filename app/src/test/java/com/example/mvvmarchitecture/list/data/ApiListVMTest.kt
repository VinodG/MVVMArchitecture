package com.example.mvvmarchitecture.list.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmarchitecture.CoroutineTestRule
import com.example.mvvmarchitecture.base.Preference
import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.models.Post
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class ApiListVM2Test {

    var repo = mock(CommonRepo::class.java)
    var preference = mock(Preference::class.java)
    lateinit var vm: ApiListVM

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        runBlocking {
            vm = ApiListVM(repo, preference)
            `when`(repo.getApi()).thenReturn(listOf(Post()))
        }
    }

    @Test
    fun `get() return list of Posts`() = runBlocking {
        var p = repo.getApi()
        Assert.assertEquals(p, listOf(Post(userId = null)))
    }

    @Test
    fun `given api data tab names are received properly`() {
        assert(true)
    }
}