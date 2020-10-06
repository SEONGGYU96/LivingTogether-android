package com.seoultech.livingtogether_android

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoultech.livingtogether_android.database.LivingTogetherDatabase
import com.seoultech.livingtogether_android.device.data.source.DeviceLocalDataSource
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinLocalDataSource
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinRepository
import com.seoultech.livingtogether_android.signal.SignalHistoryRepository
import com.seoultech.livingtogether_android.user.data.source.ProfileRepository
import com.seoultech.livingtogether_android.user.data.source.local.ProfileLocalDataSource
import com.seoultech.livingtogether_android.user.data.source.remote.ProfileRemoteDataSource
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

    @SuppressLint("HardwareIds")
    fun provideProfileRepository(context: Context): ProfileRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return ProfileRepository.getInstance(
            ProfileLocalDataSource.getInstance(AppExecutors(), database.profileDao()),
            ProfileRemoteDataSource(Firebase.database.reference.child("Users").child(deviceId).child("profile")))
    }
}