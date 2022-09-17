package com.example.mvvmarchitecture.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.databinding.ActivityMyViewpagerBinding

class MyViewpagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyViewpagerBinding
    private val adapter by lazy {
        MyPagerAdapter(supportManger = supportFragmentManager)
    }
    private var isShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_viewpager)
        binding.vp.adapter = adapter
        binding.btnSwap.setOnClickListener {
            isShowing = isShowing.not()
            adapter.refresh(isShowing)
        }
        binding.vp.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {


            }

            override fun onPageSelected(position: Int) {
                Log.e(TAG, "onPageSelected: $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private val TAG = "MyViewpagerActivity"
}