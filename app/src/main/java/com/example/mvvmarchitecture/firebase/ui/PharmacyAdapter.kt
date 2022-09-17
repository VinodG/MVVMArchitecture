package com.example.mvvmarchitecture.firebase.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.databinding.ItemPharmacyBinding
import com.example.mvvmarchitecture.firebase.models.Pharmacy
import javax.inject.Inject

class PharmacyAdapter @Inject constructor() : RecyclerView.Adapter<PharmacyAdapter.ViewHolder>() {

    var list: List<Pharmacy> = listOf()

    inner class ViewHolder(var binding: ItemPharmacyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pharmacy: Pharmacy) {
            binding.pharmacy = pharmacy
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_pharmacy,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])
    override fun getItemCount() = list.size
    fun refresh(list: List<Pharmacy>): Unit {
        this.list = list
        notifyDataSetChanged()
    }
}