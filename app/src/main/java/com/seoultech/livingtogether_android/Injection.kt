package com.seoultech.livingtogether_android

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoultech.livingtogether_android.database.LivingTogetherDatabase
import com.seoultech.livingtogether_android.device.data.source.local.DeviceLocalDataSource
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository
import com.seoultech.livingtogether_android.device.data.source.remote.DeviceRemoteDataSource
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinLocalDataSource
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinRepository
import com.seoultech.livingtogether_android.signal.SignalHistoryRepository
import com.seoultech.livingtogether_android.user.data.source.ProfileRepository
import com.seoultech.livingtogether_android.user.data.source.local.ProfileLocalDataSource
import com.seoultech.livingtogether_android.user.data.source.remote.ProfileRemoteDataSource
import com.seoultech.livingtogether_android.util.AppExecutors

object Injection {

    private var remoteDatabase: DatabaseReference? = null

    private fun getRemoteDatabase(context: Context): DatabaseReference {
        if (remoteDatabase == null) {
            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            remoteDatabase = Firebase.database.reference.child("Users").child(deviceId)
        }
        return remoteDatabase!!
    }

    fun provideNextOfKinRepository(context: Context): NextOfKinRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return NextOfKinRepository.getInstance(
            NextOfKinLocalDataSource.getInstance(AppExecutors(), database.nokDao()))
    }

    fun provideDeviceRepository(context: Context): DeviceRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return DeviceRepository.getInstance(
            DeviceLocalDataSource.getInstance(AppExecutors(), database.deviceDao()), DeviceRemoteDataSource(
                getRemoteDatabase(context).child("device")))
    }

    fun provideSignalHistoryRepository(context: Context): SignalHistoryRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        //Todo: 다른 레포지토리 처럼 수정
        return SignalHistoryRepository()
    }

    @SuppressLint("HardwareIds")
    fun provideProfileRepository(context: Context): ProfileRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return ProfileRepository.getInstance(
            ProfileLocalDataSource.getInstance(AppExecutors(), database.profileDao()),
            ProfileRemoteDataSource(getRemoteDatabase(context).child("profile")))
    }
}