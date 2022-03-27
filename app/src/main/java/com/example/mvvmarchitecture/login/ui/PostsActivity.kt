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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding

    @Inject
    lateinit var postAdapter: PostAdapter

    val vm: PostViewModel by viewModels()

    @Inject
    lateinit var alert: CommonDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post)
        setObservers()
        getPost()
        setListeners()
    }


    private fun setListeners() {
        binding.apply {
            tvRetry.setOnClickListener {
                getPost()
            }
            rv.adapter = postAdapter
            etSearch.addTextChangedListener {

            }
        }
    }

    private fun setObservers() {
    }


    private fun getPost() = performOnInternet({
        binding.showError = true
    }) {
        vm.getPost()
    }


}