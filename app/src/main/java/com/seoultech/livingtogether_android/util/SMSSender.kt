package com.seoultech.livingtogether_android.util

import android.telephony.SmsManager
import android.util.Log
import com.seoultech.livingtogether_android.nok.repository.NOKRepository

/**
 * SMS를 전송하기 위한 유틸 클래스
 */
object SMSSender {
    /**
     * SMS를 특정인에게 전송하는 메서드
     * @param address 목적지 주소
     * @param content 내용
     */
    private fun sendSMS(address: String?, content: String?) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(address, null, content, null, null)
        } catch (e: Exception) {
            Log.e("SMSSender.class", "Send is fail. ${e.message}")
        }
    }

    /**
     * SMS를 등록한 연락처들 모두에게 전송하는 메서드
     * @param content 내용
     */
    fun sendSMSAll(content: String?) {
        val contactList = NOKRepository().getAll()
        for (contact in contactList) {
            Log.e("SMSSender.class", contact.toString())
            sendSMS(contact.phoneNum, content)
        }
    }
}
