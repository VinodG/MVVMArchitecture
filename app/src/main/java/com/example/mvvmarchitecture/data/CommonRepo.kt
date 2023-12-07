package com.example.mvvmarchitecture.data

import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Api
import com.example.mvvmarchitecture.data.remote.LocalApi
import com.example.mvvmarchitecture.data.remote.MyResult
import com.example.mvvmarchitecture.data.remote.asResult
import com.example.mvvmarchitecture.data.remote.safeApi
import com.example.mvvmarchitecture.multilevel.Network
import com.example.mvvmarchitecture.multilevel.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class CommonRepo @Inject constructor(private var api: Api, private val localApi: LocalApi) : Repo {
    override suspend fun getApi() = api.get()
    override suspend fun getLocalPosts(): MyResult<List<Post>?> {
        return safeApi { localApi.getPosts() }
    }

    suspend fun getX(): Flow<MyResult<List<Post>?>> =
        getLocalPosts().asResult().flowOn(Dispatchers.IO)

    override fun getList() = flow<Network> {
        var list = mutableListOf<Product>()
        var i = 1
        emit(Network.Load)
        delay(2000)
        repeat(2) {
            repeat(2) { category ->
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
    }.flowOn(Dispatchers.IO)

    override suspend fun getResponseBody(): Flow<MyResult<ResponseBody?>> {
        val x = safeApi { localApi.getResponseList() }
        val y = x.asResult()
        return y.flowOn(Dispatchers.IO)
    }

    suspend fun getJson() = localApi.get()
    suspend fun postImage(filePart: MultipartBody.Part, textPart: RequestBody) =
        localApi.postImage(filePart, textPart)

    suspend fun getPoly() = localApi.getPolyObject()

}

class CommonRepo2 @Inject constructor(private var api: Api) : Repo {
    override suspend fun getApi() = listOf(Post(title = "vinod"))
    override suspend fun getLocalPosts(): MyResult<List<Post>?> {
        return MyResult.Success(null)
    }

    override fun getList(): Flow<Network> {
        return flow<Network> { emit(Network.Data(listOf())) }
    }

    override suspend fun getResponseBody(): Flow<MyResult<ResponseBody?>>{
        TODO("Not yet implemented")
    }
}

interface Repo {
    suspend fun getApi(): List<Post>
    suspend fun getLocalPosts(): MyResult<List<Post>?>
    fun getList(): Flow<Network>
    suspend fun getResponseBody(): Flow<MyResult<ResponseBody?>>
}

