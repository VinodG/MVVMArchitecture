package com.example.mvvmarchitecture.firebase.datasources

import com.example.mvvmarchitecture.firebase.models.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PharmacyDataSource @Inject constructor(private var database: FirestoreData) :
    BaseDataSource<Pharmacy> {
    init {
        database.path = "pharmacy"
    }
    override fun add(data: Pharmacy) = database.add(data)
    override fun onChange(): Flow<ApiState<out List<Pharmacy>>> = database.onChange()

}
