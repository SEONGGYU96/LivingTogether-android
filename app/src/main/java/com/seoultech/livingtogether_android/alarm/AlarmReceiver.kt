package com.seoultech.livingtogether_android.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Alarm is received successfully")
        //Todo: 비상상황 발생 시 프로토콜 실행
    }
}