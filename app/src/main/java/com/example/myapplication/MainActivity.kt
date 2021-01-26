package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val permissions = Arrays.asList(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val REQUEST_PERMISSION_CODE = 11
    private var permissionRequestCount: Int = 0
    private val MAX_NUMBER_REQUEST_PERMISSIONS = 2

    private val viewModel by viewModels<WorkViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionsIfNecessary()
        viewModel.initLiveData()
        initUiAndLiveData()

        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel.startWorkerChain()
            if (!viewModel.isInit) {
                viewModel.isInit = true
            }
        }
        findViewById<Button>(R.id.button1).setOnClickListener {
            viewModel.cancelAllWork()
        }
    }

    private fun initUiAndLiveData() {

        val text1 = findViewById<TextView>(R.id.text1)
        val text2 = findViewById<TextView>(R.id.text2)
        val text3 = findViewById<TextView>(R.id.text3)

        viewModel.getLiveData().observe(this, {

            when (it.first) {
                GET_URL_WORKER -> {
                    val workInfo = it.second
                    Log.d("asdadasdasdasda", workInfo.toString())
                    var str = "getUrlWorker  mState = ${workInfo.state}"
                    if (workInfo.progress.getString("Status") != null) {
                        str = "$str progress = ${workInfo.progress.getString("Status")}"
                    }
                    text1.text = str
                }
                DOWNLOAD_IMAGE_INTO_BITMAP -> {
                    val workInfo = it.second

                    var str = "downloadImageIntoBitmapWorker  mState = ${workInfo.state}"
                    if (workInfo.progress.getString("Status") != null) {
                        str = "$str progress = ${workInfo.progress.getString("Status")}"
                    }
                    text2.text = str
                }
                SAVE_IMAGE_TO_FILE -> {

                    val workInfo = it.second

                    var str = "saveImageToFileWorker  mState = ${workInfo.state}"
                    if (workInfo.progress.getString("Status") != null) {
                        str = "$str progress = ${workInfo.progress.getString("Status")}"
                    }
                    text3.text = str

                }
            }

        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            requestPermissionsIfNecessary()
        }
    }

    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            if (permissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                permissionRequestCount += 1
                ActivityCompat.requestPermissions(
                    this,
                    permissions.toTypedArray(),
                    REQUEST_PERMISSION_CODE
                )
            } else {
                Toast.makeText(
                    this,
                    "没权限",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in permissions) {
            hasPermissions = hasPermissions && isPermissionGranted(permission)
        }
        return hasPermissions
    }

    private fun isPermissionGranted(permission: String) = ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}