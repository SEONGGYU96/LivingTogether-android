package com.seoultech.livingtogether_android.util

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast

object BluetoothUtil {

    //TODO: Application 클래스를 만들어 거기서 ApplicationContext를 넘겨받기
    fun isBluetoothAvailable(context: Context): Boolean {

        fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

        //BLE 지원하는지 확인
        context.packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) } ?: return false

        return true
    }

}