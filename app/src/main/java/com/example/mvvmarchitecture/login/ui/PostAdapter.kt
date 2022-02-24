//package com.example.mvvmarchitecture.login.ui
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.example.mvvmarchitecture.R
//import com.example.mvvmarchitecture.data.models.Post
//import com.example.mvvmarchitecture.databinding.ItemPostBinding
//import javax.inject.Inject
//
//class PostAdapter @Inject constructor(
//    var list: List<Post>
//) :
//    RecyclerView.Adapter<PostAdapter.ViewHolder>() {
//
//    private var callBack: ((Int) -> Unit)? = null
//
//    class ViewHolder(var binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(position: Int, list: List<Post>, callBack: ((Int) -> Unit)?) {
//            binding.apply {
//                post = list[position]
//                root.setOnClickListener {
//                    callBack?.invoke(position)
//                }
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
//        DataBindingUtil.inflate(
//            LayoutInflater.from(parent.context),
//            R.layout.item_post,
//            parent,
//            false
//        )
//    )
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
//        holder.bind(position, list, callBack)
//
//    override fun getItemCount() = list.size
//
//    fun refresh(list: List<Post>, callBack: ((Int) -> Unit)? = null) {
//        this.list = list
//        this.callBack = callBack
//        notifyDataSetChanged()
//    }
//}