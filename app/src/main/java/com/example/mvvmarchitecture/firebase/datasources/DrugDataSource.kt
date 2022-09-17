package com.example.mvvmarchitecture.firebase.datasources

import com.example.mvvmarchitecture.firebase.models.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DrugDataSource @Inject constructor(private var database: FirestoreData) :
    BaseDataSource<Drug> {
    init {
        database.path = "drugs"
    }

    override fun add(data: Drug) = database.add(data)
    override fun onChange(): Flow<ApiState<out List<Drug>>> = database.onChange()

    fun onQuery(query: String): Flow<ApiState<out List<Drug>>> = callbackFlow {
        database.dbRef.collection(database.path)
            .orderBy("name")
            .startAt(query)
            .endAt(query + '\uf8ff').get()
            .addOnSuccessListener {
                println("TESTING__ ${it.documents.size}")
                trySend(ApiState.Success(it.getList<Drug>()))
            }.addOnFailureListener {
                trySend(ApiState.Error<List<Drug>>(it))
            }
        awaitClose()
    }
}
