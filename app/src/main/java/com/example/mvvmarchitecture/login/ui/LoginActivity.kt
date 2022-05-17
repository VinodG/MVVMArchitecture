package com.example.mvvmarchitecture.login.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.toSpannable
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.base.AppViewModel
import com.example.mvvmarchitecture.base.Preference
import com.example.mvvmarchitecture.data.models.Post
import com.example.mvvmarchitecture.data.remote.Results
import com.example.mvvmarchitecture.databinding.ActivityLoginBinding
import com.example.mvvmarchitecture.extension.performOnInternet
import com.example.mvvmarchitecture.extension.toast
import com.example.mvvmarchitecture.login.viewmodela.LoginVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val vm: LoginVM by viewModels()

    @Inject
    lateinit var postAdapter: PostAdapter
    val tagAdapter: TagsAdapter by lazy { TagsAdapter(listOf()) }

    @Inject
    lateinit var preference: Preference

    @Inject
    lateinit var appViewModel: AppViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appViewModel.x = 3
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setObservers()
        getPost()
        setListeners()
        lifecycleScope.launch {
            preference.getToken {
                println("token $it")
            }
        }
    }


    private fun setListeners() {
        binding.apply {
            tvRetry.setOnClickListener {
                getPost()
            }
            rv.adapter = tagAdapter
//            rv.adapter = postAdapter
            etSearch.addTextChangedListener {
                if (it == null)
                    return@addTextChangedListener
                vm.filter(it.toSpannable())
            }
        }
    }

    private fun setObservers() {
        vm.lvPost.observe(this) {
            binding.apply {
                when (it) {
                    is Results.Data -> {
                        showError = false
                        setPosts(vm.arrTemp)
                    }
                    is Results.Error -> {
                        showError = true
                    }
                    else -> {
                    }
                }
            }

        }
        vm.lvLoader.observe(this) {
            binding.loading = it
        }
        lifecycleScope.launchWhenStarted {
            vm.tags.collect {
                when (it) {
                    is Results.Loading -> {
                        binding.loading = true
                    }
                    is Results.Data -> {
                        binding.loading = false
                        tagAdapter.refresh(it.data)
                    }
                }

            }
        }
        lifecycleScope.launchWhenStarted {
            vm.tagText.collect {
                binding.tvPreview.text = it
            }
        }
    }

    private fun setPosts(arrTemp: List<Post>) {
        postAdapter.refresh(arrTemp) {
            val post = arrTemp[it]
            toast(post.body ?: "")
        }
    }

    private fun getPost() = performOnInternet({ binding.showError = true }) {
        vm.getPost()
    }


}
