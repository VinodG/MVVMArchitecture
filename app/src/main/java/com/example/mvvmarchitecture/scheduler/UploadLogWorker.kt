package com.example.mvvmarchitecture.scheduler

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mvvmarchitecture.data.CommonRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@HiltWorker
class UploadLogWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repo: CommonRepo
) : CoroutineWorker(context, params) {
    private val TAG = "UploadLogWorker"
    override suspend fun doWork(): Result =
        withContext(Dispatchers.IO) {
            Log.e(TAG, "doWork: onStart")
            repo.getContacts().collect {
                repo.addToBackend(it)
            }
            println("api call from work manager")
            Log.e(TAG, "doWork: end")
            Result.Success()
        }

}