package com.example.mvvmarchitecture.data

import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.multilevel.Network
import com.example.mvvmarchitecture.multilevel.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CommonRepo @Inject constructor(private var api: Api) : Repo {
    override suspend fun getApi() = api.get()

    //    override fun getList(): kotlinx.coroutines.flow.Flow<Network> {
    override fun getList() = flow<Network> {
        var list = mutableListOf<Product>()
        var i = 1
        delay(2000)
        repeat(3) { product ->
            repeat(5) { category ->
                repeat(2) { subCategory ->
                    repeat(2) { subSubCategory ->
                        list.add(
                            Product(
                                id = i.toString(),
                                name = "Product name - $i ",
                                date = "$i/01/2022",
                                expiredDate = "$i/02/2022",
                                status = "status-${(i % 3) + 1}",
                                category = "category-${category + 1}",
                                subCategory = "subCategory-${subCategory + 1}",
                                subSubCategory = "subSubCategory-${subSubCategory + 1}"
                            )
                        )
                        i++
                    }
                }
            }
        }
        println("loading")
        emit(Network.Data(list))
        println("done")
    }

}

class CommonRepo2 @Inject constructor(private var api: Api) : Repo {
    override suspend fun getApi() = listOf(Post(title = "vinod"))
    override fun getList(): kotlinx.coroutines.flow.Flow<Network> {
        return flow<Network> { emit(Network.Data(listOf())) }
    }
}

interface Repo {
    suspend fun getApi(): List<Post>
    fun getList(): kotlinx.coroutines.flow.Flow<Network>
}

