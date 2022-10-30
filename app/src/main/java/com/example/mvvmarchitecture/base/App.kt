package com.example.mvvmarchitecture.base

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mvvmarchitecture.scheduler.UploadLogWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        setupRecurringWork()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setWorkerFactory(workerFactory).build()
    }

    private fun setupRecurringWork() {
        val repeatingRequest =
            PeriodicWorkRequestBuilder<UploadLogWorker>(1, TimeUnit.MINUTES).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "name",
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}