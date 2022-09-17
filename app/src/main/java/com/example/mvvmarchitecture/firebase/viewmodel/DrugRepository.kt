package com.example.mvvmarchitecture.firebase.viewmodel

import com.example.mvvmarchitecture.firebase.datasources.DrugDataSource
import com.example.mvvmarchitecture.firebase.models.Drug
import com.example.mvvmarchitecture.firebase.models.Pharmacy
import javax.inject.Inject


class DrugRepository @Inject constructor(
    var drugs: DrugDataSource
) {
    fun getDrugs() = drugs.onChange()
    fun addDrug(drug: Drug) = this.drugs.add(drug)
    fun onQuery(query: String) = drugs.onQuery(query)
}