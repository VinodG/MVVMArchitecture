package com.example.mvvmarchitecture.firebase.models

import com.google.firebase.firestore.QuerySnapshot


inline fun <reified T> QuerySnapshot.getList(): List<T> {
    return this.documents.mapNotNull { it.toObject(T::class.java) }
}