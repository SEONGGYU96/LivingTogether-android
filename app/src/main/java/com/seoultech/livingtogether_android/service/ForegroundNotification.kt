package com.seoultech.livingtogether_android.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.ui.main.MainActivity

class ForegroundNotification(private val application: Application) {
    companion object {
        private const val CHANNEL_ID = "LivingTogether_channel"
        private const val CHANNEL_NAME = "LivingTogether_channel"
    }

    private val notificationManager: NotificationManager by lazy {
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    init {
        //Android 8.0 이상은 채널을 할당해주어야 한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            //앱 아이콘에서 알림이 뜨는 기능. 포그라운드 서비스를 위함이므로 사용안함
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getNotification(state: Boolean): Notification {
        val text: String = when (state) {
            true -> application.getString(R.string.notification_service_ok)
            false -> application.getString(R.string.notification_bluetooth_off)
        }

        val notificationIntent = Intent(application, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val requestID = System.currentTimeMillis().toInt()

        val pendingIntent = PendingIntent.getActivity(
            application, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(application, CHANNEL_ID)

        builder.setContentTitle(application.getString(R.string.app_name))
            //.setSmallIcon(R.drawable.) Todo: 아이콘 넣기
            .setContentText(text) //알림 타이틀
            .setShowWhen(false) //알림 시간 노출 여부
            .setContentIntent(pendingIntent)

        if (state) {
            builder.color = ContextCompat.getColor(application, R.color.mainBlue)
        } else {
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(text)) //긴 문자열 스타일, 문구 지정
        }

        return builder.build()
    }
}