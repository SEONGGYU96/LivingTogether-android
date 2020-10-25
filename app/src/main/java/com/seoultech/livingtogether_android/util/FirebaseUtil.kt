package com.seoultech.livingtogether_android.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoultech.livingtogether_android.ApplicationImpl

object FirebaseUtil {
    private var remoteDatabase: DatabaseReference? = null
    private const val TAG = "FirebaseUtil"

    @SuppressLint("HardwareIds")
    fun getRemoteDatabase(context: Context): DatabaseReference {
        if (remoteDatabase == null) {
            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            remoteDatabase = Firebase.database.reference.child("Users").child(deviceId)
        }
        return remoteDatabase!!
    }

    fun pushEmergency(isInit: Boolean) {
        if (isInit) {
            getRemoteDatabase(ApplicationImpl.getInstance()).child("emergency").child("time").setValue(false)
        } else {
            val time = StringUtil.getCurrentTime(year = true, longYear = true, time = true)
            getRemoteDatabase(ApplicationImpl.getInstance()).child("emergency").child("time").setValue(time)
        }
        Log.d(TAG, "Push emergency time to server")
    }
}