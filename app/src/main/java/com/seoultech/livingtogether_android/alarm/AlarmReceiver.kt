package com.seoultech.livingtogether_android.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.seoultech.livingtogether_android.util.SMSSender

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Alarm is received successfully")
        SMSSender.sendSMSAll("테스트입니다.")
    }
}