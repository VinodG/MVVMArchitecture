package com.example.mvvmarchitecture.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.mvvmarchitecture.R

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        println("NavigationActivity : onCreate")
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }
    override fun onStart() {
        super.onStart()
        println("NavigationActivity : onStart")
    }

    override fun onPause() {
        super.onPause()
        println("NavigationActivity : onPause")
    }

    override fun onResume() {
        super.onResume()
        println("NavigationActivity : onResume")
    }

}

