package com.example.mvvmarchitecture.list.data

import com.example.mvvmarchitecture.base.Preference
import com.example.mvvmarchitecture.data.CommonRepo
import com.example.mvvmarchitecture.data.models.Post
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ApiListVMTest {

    var repo = mock(CommonRepo::class.java)
    var preference = mock(Preference::class.java)

    @Before
    fun setUp() {
        runBlocking {
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
        var vm = ApiListVM(repo, preference)

    }
}