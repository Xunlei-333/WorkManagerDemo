package com.example.myapplication.worker

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.myapplication.KEY_IMAGE_URI
import com.example.myapplication.getSleepTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

private val Title = "DownLoaded Image"
private val dateFormatter = SimpleDateFormat(
    "yyyy.MM.dd 'at' HH:mm:ss z",
    Locale.getDefault()
)

class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {

        return withContext(Dispatchers.IO) {
            delay(getSleepTime())

            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val resolver = applicationContext.contentResolver
            val bitmap = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))

            MediaStore.Images.Media.insertImage(resolver, bitmap, Title, dateFormatter.format(Date()))

            setProgress(workDataOf("Status" to "SaveImageToFileWorker Finish Work"))
            delay(getSleepTime())

            Result.success()

        }
    }
}