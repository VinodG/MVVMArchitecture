package com.example.mvvmarchitecture.temp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmarchitecture.CoroutineTestRule
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.internal.wait
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class TempVMTest {

    private val currentDate: Date = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2022)
        set(Calendar.MONTH, 1)
        set(Calendar.DAY_OF_MONTH, 1)

    }.time
    lateinit var tr: TempRepo
    lateinit var vm: TempVM
    var resManager = mock(TempResManager::class.java)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @Before
    fun setUp() {
        tr = TempRepo()
        Dispatchers.setMain(mainThreadSurrogate)
        vm = TempVM(tr, TempResManager(), mainThreadSurrogate, currentDate = currentDate)
    }

    @Test
    fun testMembers() {
        testDispatcher.runBlockingTest {
            vm.getPost()
            var list = vm.tabNames.getOrAwaitValue()
            println(list)
            assert(list.isNotEmpty())
        }
    }

    @Test
    fun testSuccessResponse() {
        vm.getPost()
        vm.apiResult.getOrAwaitValue()
        var list = vm.apiResult.getOrAwaitValue()
        var isSuccessReceived = false
        var latch = CountDownLatch(2)
        vm.apiResult.observeForever {
            println("$it -> ")
            if (it is Results.Data) {
                isSuccessReceived = true
                list = it
            }
        }
        latch.await(8, TimeUnit.SECONDS)
        assert(isSuccessReceived)
        assert(list is Results.Data)
    }

    @Test
    fun testErrorResponse() = mainCoroutineRule.runBlockingTest {
        tr = mock(TempRepo::class.java)
        Dispatchers.setMain(mainThreadSurrogate)
        vm = TempVM(tr, TempResManager(), mainThreadSurrogate, currentDate = currentDate)
        `when`(vm.repo.getApi()).then { throw Exception("timeout") }
        vm.getPost()
        var isErrorReceived = false
        var latch = CountDownLatch(2)
        vm.apiResult.observeForever {
            println("$it -> ")
            if (it is Results.Error) {
                isErrorReceived = true
            }
        }
        latch.await(8, TimeUnit.SECONDS)
        assert(isErrorReceived)
        println("END")
    }

    @Test
    fun testResourceManager() {
        `when`(resManager.getString(anyInt())).thenReturn("Vinod")
        var str = vm.getString()
//        assert(str == "Vinod")
        assert(str == "testing")
    }

    @Test
    fun testDelayFunction() {
        mainCoroutineRule.runBlockingTest {
            launch {
                vm.testDelay(4000)
                var latch = CountDownLatch(1)
                vm.delayLiveData.observeForever {
                    println(" value -> $it")
                }
                latch.await(10, TimeUnit.SECONDS)

            }
        }
    }

    @Test
    fun `add Days test`() {
        var futureDate = vm.getDate(6, 1, 2022, "yyyy-MM-dd")
        assertEquals(SimpleDateFormat("yyyy-MM-dd").format(vm.addDays(5)), futureDate)
    }
}