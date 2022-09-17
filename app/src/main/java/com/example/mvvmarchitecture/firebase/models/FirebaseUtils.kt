package com.example.mvvmarchitecture.firebase.models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.QuerySnapshot


inline fun <reified T> DataSnapshot.getList(): List<T> {
    return this.children.mapNotNull { it.getValue(T::class.java) }
}

inline fun <reified T> QuerySnapshot.getList(): List<T> {
    return this.documents.mapNotNull { it.toObject(T::class.java) }
}