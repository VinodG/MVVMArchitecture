package com.example.mvvmarchitecture.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PermissionUtils @Inject constructor(@ActivityContext val context: Context) {
    var result: MutableLiveData<Result> = MutableLiveData(Result.None)

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkSmsPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                result.value = (Result.SMS(true))
            }
            (context as Activity).shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS) -> {
                Toast.makeText(context, "Give permission to read sms", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_SMS)
            }
        }

    }

    private val requestPermissionLauncher =
        (context as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            result.value = (Result.SMS(isGranted))
        }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkContactPermission() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                result.value = (Result.Contact(true))
            }
            (context as Activity).shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS) -> {
                Toast.makeText(context, "Give permission to read Phone contact", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestContactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }

    }

    private val requestContactPermissionLauncher =
        (context as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            result.value = (Result.Contact(isGranted))
        }

    sealed class Result {
        data class SMS(val isGranted: Boolean) : Result()
        data class Contact(val isGranted: Boolean) : Result()
        object None : Result()
    }
}