package com.seoultech.livingtogether_android.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.seoultech.livingtogether_android.user.data.source.ProfileRepository
import com.seoultech.livingtogether_android.util.SMSSender

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        sendSMS()
    }

    private fun sendSMS() {
        //var name = ProfileRepository().getAll().name

//        if (name.isEmpty()) {
//            name = "아무개"
//        }
        val emergencyTime = 12

        Log.d(TAG, "Alarm is received successfully")
        //SMSSender.sendSMSAll("[LivingTogether 비상 상황 알림 서비스]\n${name}님의 거주지 내 활동이 ${emergencyTime}시간 동안 감지되지 않았습니다.")
    }
}