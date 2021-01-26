package com.example.myapplication

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.myapplication.worker.DownloadImageIntoBitmapWorker
import com.example.myapplication.worker.GetUrlWorker
import com.example.myapplication.worker.SaveImageToFileWorker
import java.util.*

const val TAG = "WorkManagerDemo"

class WorkViewModel(application: Application) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    private var getUrlWorker: OneTimeWorkRequest? = null
    private var downloadImageIntoBitmapWorker: OneTimeWorkRequest? = null
    private var saveImageToFileWorker: OneTimeWorkRequest? = null

    private val pref = application.getSharedPreferences("data", MODE_PRIVATE)
    private val editor = pref.edit()

    var isInit = false

    fun startWorkerChain() {

        getUrlWorker = OneTimeWorkRequestBuilder<GetUrlWorker>()
            .addTag(TAG)
            .build()

        downloadImageIntoBitmapWorker =
            OneTimeWorkRequestBuilder<DownloadImageIntoBitmapWorker>()
                .addTag(TAG)
                .build()

        saveImageToFileWorker =
            OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
                .addTag(TAG)
                .build()

        editor.apply {
            putString(GET_URL_WORKER, getUrlWorker!!.id.toString())
            putString(DOWNLOAD_IMAGE_INTO_BITMAP, downloadImageIntoBitmapWorker!!.id.toString())
            putString(SAVE_IMAGE_TO_FILE, saveImageToFileWorker!!.id.toString())
            apply()
        }


        initLiveData()

        val continuation =
            workManager.beginWith(getUrlWorker!!)
                .then(downloadImageIntoBitmapWorker!!)
                .then(saveImageToFileWorker!!)

        continuation.enqueue()

    }

    fun cancelAllWork() {
        if (!isInit) return

        liveData.removeSource(getLiveDataUsingUUIDString(GET_URL_WORKER)!!)
        liveData.removeSource(getLiveDataUsingUUIDString(DOWNLOAD_IMAGE_INTO_BITMAP)!!)
        liveData.removeSource(getLiveDataUsingUUIDString(SAVE_IMAGE_TO_FILE)!!)
        workManager.cancelAllWorkByTag(TAG)
    }

    private val liveData: MediatorLiveData<Pair<String, WorkInfo>> = MediatorLiveData()

    fun initLiveData() {
        bindUUIDAndLiveData(GET_URL_WORKER)
        bindUUIDAndLiveData(DOWNLOAD_IMAGE_INTO_BITMAP)
        bindUUIDAndLiveData(SAVE_IMAGE_TO_FILE)
    }

    fun getLiveData(): LiveData<Pair<String, WorkInfo>> {
        return liveData
    }

    private fun getUUIDStringFromSharedPreferences(key: String): String {
        return pref.getString(key, "")!!
    }

    private fun bindUUIDAndLiveData(name: String) {
        val uuidString = getUUIDStringFromSharedPreferences(name)
        if (uuidString != ""){
            liveData.addSource(workManager.getWorkInfoByIdLiveData(UUID.fromString(uuidString))) {
                liveData.value = name to it
            }
        }
        val tmp = getLiveDataUsingUUIDString(name)
        if (tmp != null) {
            liveData.addSource(tmp) {
                liveData.value = name to it
            }
        }
    }
    private fun getLiveDataUsingUUIDString(name: String): LiveData<WorkInfo>? {
        val uuidString = getUUIDStringFromSharedPreferences(name)
        if (uuidString != ""){
            return workManager.getWorkInfoByIdLiveData(UUID.fromString(uuidString))
        }
        return null
    }
}