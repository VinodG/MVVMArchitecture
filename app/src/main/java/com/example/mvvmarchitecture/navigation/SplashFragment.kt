package com.example.mvvmarchitecture.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.databinding.FragmentHomeBinding
import com.example.mvvmarchitecture.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.action_splash_to_home)
        }
        binding.btnOrderDetails.setOnClickListener {
            var request = NavDeepLinkRequest.Builder.fromUri(
                "app://com.example.order/orderdetails".toUri()
            ).build()
            findNavController().navigate(request)
        }
    }

}