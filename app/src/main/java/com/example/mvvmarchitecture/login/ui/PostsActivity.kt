package com.example.mvvmarchitecture.login.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.base.CommonDialog
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.databinding.ActivityPostBinding
import com.example.mvvmarchitecture.extension.performOnInternet
import com.example.mvvmarchitecture.extension.toast
import com.example.mvvmarchitecture.login.viewmodela.LoginVM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    val vm: LoginVM by viewModels()

    @Inject
    lateinit var postAdapter: PostAdapter

    @Inject
    lateinit var alert: CommonDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)
        setObservers()
        getPost()
        setListeners()
        alert.body("message", header = "vinod", negCallBack = {
            alert.dismiss()
        }).show()

    }


    private fun setListeners() {
        binding.apply {
            tvRetry.setOnClickListener {
                getPost()
            }
            rv.adapter = postAdapter
            etSearch.addTextChangedListener {
                vm.filter(it.toString())
            }
        }
    }

    private fun setObservers() {
        vm.lvPost.observe(this, {
            binding.apply {
                when (it) {
                    is Results.Data -> {
                        showError = false
                        setPosts(vm.arrTemp)
                    }
                    is Results.Error -> {
                        showError = true
                    }
                }
            }

        })
        vm.lvLoader.observe(this, {
            binding.loading = it
        })
    }

    private fun setPosts(arrTemp: List<Post>) {
        postAdapter.refresh(arrTemp) {
            var post = arrTemp[it]
            toast(post.body ?: "")
        }
    }

    private fun getPost() = performOnInternet({
        binding.showError = true
    }) {
        vm.getPost()
    }


}