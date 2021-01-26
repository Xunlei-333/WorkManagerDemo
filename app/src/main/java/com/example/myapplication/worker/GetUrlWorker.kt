package com.example.myapplication.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.myapplication.KEY_IMAGE_URI_FROM_API
import com.example.myapplication.getSleepTime
import com.example.myapplication.net.NetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class GetUrlWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                setProgress(workDataOf("Status" to "GetUrlWorker Start But Sleep"))
                delay(getSleepTime())
                setProgress(workDataOf("Status" to "GetUrlWorker End Sleep"))

                setProgress(workDataOf("Status" to "GetUrlWorker Start Real Work"))

                val data = NetRepository.getInstance().getApi().getJson()
                val outputData = workDataOf(KEY_IMAGE_URI_FROM_API to data.imgurl)

                setProgress(workDataOf("Status" to "GetUrlWorker Finish Work"))

                delay(getSleepTime())

                Result.success(outputData)
            } catch (e: Exception) {
                Result.failure()
            }

        }
    }
}