package com.seoultech.livingtogether_android.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.Injection
import com.seoultech.livingtogether_android.bluetooth.util.AlarmUtil
import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.user.data.source.ProfileDataSource
import com.seoultech.livingtogether_android.util.FirebaseUtil
import com.seoultech.livingtogether_android.util.SMSSender
import com.seoultech.livingtogether_android.util.StringUtil

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "***********EMERGENCY PROTOCOL HAS BEEN OPERATED********")
        removeReceiver()
        alertToServer()
        sendSMS()
    }

    private fun removeReceiver() {
        AlarmUtil.stopAlarm(ApplicationImpl.getInstance())
    }

    private fun alertToServer() {
        FirebaseUtil.pushEmergency(false)
    }

    private fun sendSMS() {
        val profileRepository = Injection.provideProfileRepository(ApplicationImpl.getInstance())
        profileRepository.getProfile(object : ProfileDataSource.GetProfileCallback {
            override fun onProfileLoaded(profile: Profile) {
                val name = profile.name
                val emergencyTime = 12
                SMSSender.sendSMSAll("[LivingTogether 비상 상황 알림 서비스]\n${name}님의 거주지 내 활동이 ${emergencyTime}시간 동안 감지되지 않았습니다.")
                Log.d(TAG, "***********EMERGENCY PROTOCOL HAS BEEN FINISHED********")
            }
            override fun onDataNotAvailable() { }
        })
    }
}