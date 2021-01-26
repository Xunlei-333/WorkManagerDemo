package com.example.myapplication.worker

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.myapplication.KEY_IMAGE_URI
import com.example.myapplication.KEY_IMAGE_URI_FROM_API
import com.example.myapplication.getSleepTime
import com.example.myapplication.net.NetRepository
import com.example.myapplication.writeBitmapToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class DownloadImageIntoBitmapWorker(ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            setProgress(workDataOf("Status" to "DownloadImageIntoBitmapWorker Start But Sleep"))
            delay(getSleepTime())
            setProgress(workDataOf("Status" to "DownloadImageIntoBitmapWorker End Sleep"))

            setProgress(workDataOf("Status" to "DownloadImageIntoBitmapWorker Start Real Work"))

            val url = inputData.getString(KEY_IMAGE_URI_FROM_API)
            val response = NetRepository.getInstance().getApi().getImage(url!!)
            val bitmap = BitmapFactory.decodeStream(response.byteStream())

            val outputUri = writeBitmapToFile(applicationContext, bitmap)

            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            setProgress(workDataOf("Status" to "GetUrlWorker Finish Work"))
            delay(getSleepTime())

            Result.success(outputData)

        }
    }
}