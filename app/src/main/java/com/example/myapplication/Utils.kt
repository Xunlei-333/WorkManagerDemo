package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*



const val GET_URL_WORKER = "getUrlWorker"
const val DOWNLOAD_IMAGE_INTO_BITMAP = "downloadImageIntoBitmap"
const val SAVE_IMAGE_TO_FILE = "saveImageToFile"

const val KEY_IMAGE_URI_FROM_API = "KEY_IMAGE_URI_FROM_API"
const val KEY_IMAGE_URI = "KEY_IMAGE_URI"

@Throws(FileNotFoundException::class)
fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    val name = String.format("output-%s.png", UUID.randomUUID().toString())
    val outputDir = File(applicationContext.filesDir, "outputs")
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }
    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
            }

        }
    }
    return Uri.fromFile(outputFile)
}

fun getSleepTime() = 1000L