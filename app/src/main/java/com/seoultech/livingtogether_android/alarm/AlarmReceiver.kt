package com.seoultech.livingtogether_android.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.Injection
import com.seoultech.livingtogether_android.Status
import com.seoultech.livingtogether_android.bluetooth.service.ScanService
import com.seoultech.livingtogether_android.bluetooth.util.AlarmUtil
import com.seoultech.livingtogether_android.bluetooth.util.AlarmUtil.ALARM_TYPE
import com.seoultech.livingtogether_android.bluetooth.util.AlarmUtil.DEVICE_ADDRESS
import com.seoultech.livingtogether_android.bluetooth.util.AlarmUtil.TYPE_ACTIVE
import com.seoultech.livingtogether_android.bluetooth.util.AlarmUtil.TYPE_PRESERVED
import com.seoultech.livingtogether_android.device.data.DeviceStateChangedLiveData
import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.user.data.source.ProfileDataSource
import com.seoultech.livingtogether_android.util.FirebaseUtil
import com.seoultech.livingtogether_android.util.SMSSender

class AlarmReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "AlarmReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) {
            Log.e(TAG, "intent is null")
            return
        }

        if (intent.getStringExtra(ALARM_TYPE) != TYPE_ACTIVE) {
            Log.d(TAG, "DeviceStateIsChanged by not coming preserved signal")
            val deviceAddress = intent.getStringExtra(DEVICE_ADDRESS)
            DeviceStateChangedLiveData.value = true
            Status.setConnectOk(false)
            sendBTState()
            if (deviceAddress == null) {
                Log.e(TAG, "This alarm is called by preserved But deviceAddress is null")
            } else {
                removePreservedAlarmReceiver(deviceAddress)
            }
            return
        }

        Log.d(TAG, "***********EMERGENCY PROTOCOL HAS BEEN OPERATED********")
        removeActiveAlarmReceiver()
        alertToServer()
        sendSMS()
    }

    private fun sendBTState() {
        val intent = Intent(ApplicationImpl.getInstance(), ScanService::class.java).apply {
            putExtra(ScanService.FLAG_CONNECTION, ScanService.FLAG_CONNECTION_FAIL)
        }
        ApplicationImpl.getInstance().startService(intent)
    }

    private fun removePreservedAlarmReceiver(deviceAddress: String) {
        AlarmUtil.stopPreservedAlarm(ApplicationImpl.getInstance(), deviceAddress)
    }

    private fun removeActiveAlarmReceiver() {
        AlarmUtil.stopActiveAlarm(ApplicationImpl.getInstance())
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