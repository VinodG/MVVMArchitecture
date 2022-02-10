package com.example.mvvmarchitecture.temp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmarchitecture.CoroutineTestRule
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit

@ExperimentalCoroutinesApi
class TempVMTest {

    lateinit var tr: TempRepo
    lateinit var vm: TempVM
    var resManager = mock(TempResManager::class.java)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        tr = TempRepo()
        vm = TempVM(tr, resManager)
    }

    @Test
    fun testMembers() {
        vm.getPost()
        var list = vm.tabNames.getOrAwaitValue()
        println(list)
        assert(list.isNotEmpty())
    }

    @Test
    fun testResponse() {
        vm.getPost()
        var list = vm.apiResult.getOrAwaitValue()
        println(list)
        assert(list is Results.Data<*>)
    }

    @Test
    fun testResourceManager() {
        `when`(resManager.getString(anyInt())).thenReturn("Vinod")
        var str = vm.getString()
        assert(str == "Vinod")
    }
}