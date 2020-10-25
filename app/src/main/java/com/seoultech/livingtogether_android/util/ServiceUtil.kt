package com.seoultech.livingtogether_android.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.seoultech.livingtogether_android.bluetooth.service.ScanService

object ServiceUtil {
    private const val TAG = "ServiceUtil"
    private const val RESTART = "RESTART"

    fun startService(application: Application) {
        if (!isServiceRunning(application, ScanService::class.java)) {

            startServiceDirect(application)
            Log.d(TAG, "Devices registered are exist and service is not running. start service.")
        } else {
            Log.d(TAG, "No device registered or service is already running")
        }
    }

    fun restartService(application: Application) {
        application.startService(Intent(application, ScanService::class.java).apply { putExtra(RESTART, true) })
    }

    fun stopService(application: Application) {
        application.startService(Intent(application, ScanService::class.java).apply { putExtra(ScanService.FLAG_STOP_SERVICE, true) })
    }

    private fun startServiceDirect(application: Application) {
        application.startService(Intent(application, ScanService::class.java))
    }

    @JvmStatic
    fun isServiceRunning(application: Application, serviceClass: Class<*>): Boolean {
        val manager = application.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
