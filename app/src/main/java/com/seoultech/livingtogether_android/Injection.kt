package com.seoultech.livingtogether_android

import android.content.Context
import com.seoultech.livingtogether_android.database.LivingTogetherDatabase
import com.seoultech.livingtogether_android.device.data.source.DeviceLocalDataSource
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinLocalDataSource
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinRepository
import com.seoultech.livingtogether_android.signal.SignalHistoryRepository
import com.seoultech.livingtogether_android.util.AppExecutors

object Injection {

    fun provideNextOfKinRepository(context: Context): NextOfKinRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return NextOfKinRepository.getInstance(
            NextOfKinLocalDataSource.getInstance(AppExecutors(), database.nokDao()))
    }

    fun provideDeviceRepository(context: Context): DeviceRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return DeviceRepository.getInstance(
            DeviceLocalDataSource.getInstance(AppExecutors(), database.deviceDao()))
    }

    fun provideSignalHistoryRepository(context: Context): SignalHistoryRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        //Todo: 다른 레포지토리 처럼 수정
        return SignalHistoryRepository()
    }
}