package com.example.mvvmarchitecture.firebase.viewmodel

import com.example.mvvmarchitecture.firebase.models.Pharmacy
import com.example.mvvmarchitecture.firebase.datasources.PharmacyDataSource
import javax.inject.Inject


class PharmacyRepository @Inject constructor(
    var pharmacy: PharmacyDataSource
) {
    fun getPharmacies() = pharmacy.onChange()
    fun addPharmacy(pharmacy: Pharmacy) = this.pharmacy.add(pharmacy)
}