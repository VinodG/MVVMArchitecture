package com.example.mvvmarchitecture.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import com.example.mvvmarchitecture.R
import com.example.mvvmarchitecture.databinding.ActivityGpsactivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GPSActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGpsactivityBinding
    private lateinit var manager: LocationManager

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gpsactivity)
        manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                manager.requestLocationUpdates(
                    "network",
                    50L,
                    1f
                ) {
                    var msg  ="${it.latitude}   ${it.longitude}"
                    Log.e("Testing", "onCreate: $msg")
                    binding.tvLocation.setText(msg)
                }
        }


    }
}