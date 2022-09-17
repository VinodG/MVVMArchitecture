package com.example.mvvmarchitecture.firebase.models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealDatabaseData @Inject constructor(
    var dbRef: DatabaseReference,
) {
    lateinit var path: String

    private val TAG = "FirebaseDataSource"
    fun <T> add(t: T) =
        callbackFlow<ApiState<T>> {
            throwIfPathIsEmpty()
            dbRef.child(path).push().setValue(t).addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ApiState.Success(t))
                }

            }.addOnFailureListener {
                Log.e(TAG, "addPharmacy: error ")
                trySend(ApiState.Error(it))
            }
            awaitClose()
        }

    inline fun <reified T> onChange(): Flow<ApiState<out List<T>>> = callbackFlow {
        throwIfPathIsEmpty()
        dbRef.child(path).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    var result = dataSnapshot.getList<T>()
                    trySend(ApiState.Success(result))
                } catch (e: Exception) {
                    trySend(ApiState.Error(e))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("getPharmacies: ${error.message}")
                trySend(ApiState.Error(error.toException()))
            }
        })
        awaitCancellation()
    }

    fun throwIfPathIsEmpty() {
        if (path.isEmpty()) {
            throw Exception("CustomException:  Root path should not be empty")
        }
    }


}


