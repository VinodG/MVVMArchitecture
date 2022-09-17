package com.example.mvvmarchitecture.firebase.models

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreData @Inject constructor(
    var dbRef: FirebaseFirestore,
) {
    private val TAG = "FirestoreData"
    lateinit var path: String
    fun <T> add(t: T) =
        callbackFlow<ApiState<T>> {
            throwIfPathIsEmpty()
            t?.let {
                dbRef.collection(path).add(it).addOnCompleteListener {
                    if (it.isSuccessful) {
                        trySend(ApiState.Success(t))
                    }

                }.addOnFailureListener {
                    Log.e(TAG, "addPharmacy: error ")
                    trySend(ApiState.Error(it))
                }
            }
            awaitClose()
        }

    inline fun <reified T> onChange(): Flow<ApiState<out List<T>>> = callbackFlow {
        throwIfPathIsEmpty()
        dbRef.collection(path)
            .addSnapshotListener { snapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    trySend(ApiState.Error(it.fillInStackTrace()))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.isEmpty.not()) {
                    var list: List<T> = snapshot.documents.mapNotNull { it.toObject(T::class.java) }
                    Log.e("FirestoreData", "Current data: $list")
                    trySend(ApiState.Success(list))
                } else {
                    trySend(ApiState.Success(listOf<T>()))
                }
            }
        awaitClose()
    }

    fun throwIfPathIsEmpty() {
        if (path.isEmpty()) {
            throw Exception("Root path should not be empty")
        }
    }
}
