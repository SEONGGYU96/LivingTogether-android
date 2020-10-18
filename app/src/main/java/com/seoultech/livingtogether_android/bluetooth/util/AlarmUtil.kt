package com.seoultech.livingtogether_android.bluetooth.util

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.seoultech.livingtogether_android.alarm.AlarmReceiver
import java.lang.StringBuilder

object AlarmUtil {
    private const val ACTIVE_ALARM = 200

    const val ALARM_TYPE = "alarm_type"
    const val TYPE_ACTIVE = "type_active"
    const val TYPE_PRESERVED = "type_preserved"

    const val DEVICE_ADDRESS = "device_address"

    //분 단위
    private const val ACTIVE_ALARM_TRIGGER_TIME = 240
    private const val PRESERVED_ALARM_TRIGGER_TIME = 180

    private const val TAG = "AlarmUtil"
    private var currentActivePendingIntent: PendingIntent? = null
    private var currentPreservedPendingIntent = HashMap<String, PendingIntent>()

    fun setActiveAlarm(applicationContext: Application) {
        if (currentActivePendingIntent != null) {
            stopActiveAlarm(applicationContext)
        }

        val intent = Intent(applicationContext, AlarmReceiver::class.java).apply {
            putExtra(ALARM_TYPE, TYPE_ACTIVE)
        }

        currentActivePendingIntent = PendingIntent.getBroadcast(applicationContext,
            ACTIVE_ALARM, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val triggerTime = (SystemClock.elapsedRealtime() + ACTIVE_ALARM_TRIGGER_TIME * 1000)

        setAlarm(applicationContext, currentActivePendingIntent, triggerTime)
    }

    fun setPreservedAlarm(applicationContext: Application, deviceAddress: String) {
        if (currentPreservedPendingIntent[deviceAddress] != null) {
            stopPreservedAlarm(applicationContext, deviceAddress)
        }

        val intent = Intent(applicationContext, AlarmReceiver::class.java).apply {
            putExtra(ALARM_TYPE, TYPE_PRESERVED)
            putExtra(DEVICE_ADDRESS, deviceAddress)
        }


        currentPreservedPendingIntent[deviceAddress] = PendingIntent.getBroadcast(applicationContext,
            deviceAddress.hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val triggerTime = (SystemClock.elapsedRealtime() + PRESERVED_ALARM_TRIGGER_TIME * 1000)

        setAlarm(applicationContext, currentPreservedPendingIntent[deviceAddress], triggerTime)
    }

    private fun setAlarm(applicationContext: Application, pendingIntent: PendingIntent?, triggerTime: Long) {
        (applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager)
            .set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)
        Log.d(TAG, "Alarm is registered after ${triggerTime / 1000} (seconds)")
    }

    fun stopActiveAlarm(applicationContext: Application) {
        stopAlarm(applicationContext, currentActivePendingIntent)
    }

    fun stopPreservedAlarm(applicationContext: Application, deviceAddress: String) {
        stopAlarm(applicationContext, currentPreservedPendingIntent[deviceAddress])
    }

    private fun stopAlarm(applicationContext: Application, pendingIntent: PendingIntent?) {
        if (pendingIntent == null) {
            Log.d(TAG, "Alarm is already removed")
            return
        }
        val alarmManager = applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "Alarm is removed")
    }
}