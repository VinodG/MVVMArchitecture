package com.example.mvvmarchitecture.firebase.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.databinding.ActivityPharmacyListBinding
import com.example.mvvmarchitecture.firebase.models.ApiState
import com.example.mvvmarchitecture.firebase.viewmodel.PharmacyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PharmacyListActivity : AppCompatActivity() {
    private val TAG = "PharmacyListActivity"
    private lateinit var binding: ActivityPharmacyListBinding
    private val viewModel: PharmacyViewModel by viewModels()

    @Inject
    lateinit var adapter: PharmacyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pharmacy_list)
        setUi()
        binding.rv.adapter = adapter
        binding.btnAddPharmacy.setOnClickListener { viewModel.addPharmacy() }
    }

    private fun setUi() {
        lifecycleScope.launchWhenCreated {
            viewModel.pharmacies.collect {
                Log.e(TAG, "setUi: $it")
                adapter.refresh(it)
            }
        }
    }
}