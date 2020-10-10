package com.seoultech.livingtogether_android.bluetooth.util

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.seoultech.livingtogether_android.alarm.AlarmReceiver
import com.seoultech.livingtogether_android.bluetooth.service.ScanService

object AlarmUtil {
    private const val REQUEST_ALARM = 200
    //Todo: 알람 트리거 시간 변경하여야 함. 지금은 디버깅 용도로 20초
    private const val ALARM_TRIGGER_TIME = 20
    private const val TAG = "AlarmUtil"
    private var currentPendingIntent: PendingIntent? = null

    fun setAlarm(applicationContext: Application) {
        if (currentPendingIntent != null) {
            stopAlarm(applicationContext)
        }
        val alarmManager = applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

        val intent = Intent(applicationContext, AlarmReceiver::class.java)

        currentPendingIntent = PendingIntent.getBroadcast(applicationContext,
            REQUEST_ALARM, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val triggerTime = (SystemClock.elapsedRealtime() + ALARM_TRIGGER_TIME * 1000)

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, currentPendingIntent)
        Log.d(TAG, "Alarm is registered after $ALARM_TRIGGER_TIME (seconds)")
    }

    fun stopAlarm(applicationContext: Application) {
        val alarmManager = applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(currentPendingIntent)
        Log.d(TAG, "Alarm is removed")
    }
}