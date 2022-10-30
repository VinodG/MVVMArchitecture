package com.example.mvvmarchitecture.firebase.datasources

import com.example.mvvmarchitecture.data.local.Contact
import com.example.mvvmarchitecture.firebase.models.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ContactApiDataSource @Inject constructor(private var database: FirestoreData) :
    BaseDataSource<Contact> {
    init {
        database.path = "contacts"
    }

    override fun add(data: Contact) = database.add(data)
    override fun onChange(): Flow<ApiState<out List<Contact>>> = database.onChange()

    fun onQuery(): Flow<ApiState<out List<Contact>>> = callbackFlow {
        database.dbRef.collection(database.path).get()
            .addOnSuccessListener {
                println("TESTING__ ${it.documents.size}")
                trySend(ApiState.Success(it.getList<Contact>()))
            }.addOnFailureListener {
                trySend(ApiState.Error<List<Contact>>(it))
            }
        awaitClose()
    }
}
