package com.example.mvvmarchitecture.login.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.databinding.ItemPostBinding
import com.example.mvvmarchitecture.databinding.ItemTagBinding
import javax.inject.Inject

class TagsAdapter @Inject constructor(
    var list: List<String>
) :
    RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    private var callBack: ((Int) -> Unit)? = null

    class ViewHolder(var binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, list: List<String>, callBack: ((Int) -> Unit)?) {
            binding.apply {
                tag = list[position]
                root.setOnClickListener {
                    callBack?.invoke(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_tag,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(position, list, callBack)

    override fun getItemCount() = list.size

    fun refresh(list: List<String>, callBack: ((Int) -> Unit)? = null) {
        this.list = list
        this.callBack = callBack
        notifyDataSetChanged()
    }
}