package com.example.mvvmarchitecture.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.databinding.ItemProductBinding
import com.example.mvvmarchitecture.paging.ProductsAdapter.ViewHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProductsAdapter @Inject constructor(@ApplicationContext var context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    private var _products: List<Product> = listOf()

    inner class ViewHolder(var binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.name.text = product.name
            binding.loader.visibility =  if(_products.size == product.id) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_product, parent, false)
    )

    override fun getItemCount() = _products.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(_products[position])
    }

    fun refresh(products: List<Product>) {
        _products = products
        notifyDataSetChanged()
    }

}