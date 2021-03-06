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
import com.seoultech.livingtogether_android.signal.data.source.SignalRepository
import com.seoultech.livingtogether_android.signal.data.source.local.SignalLocalDataSource
import com.seoultech.livingtogether_android.signal.data.source.remote.SignalRemoteDataSource
import com.seoultech.livingtogether_android.user.data.source.ProfileRepository
import com.seoultech.livingtogether_android.user.data.source.local.ProfileLocalDataSource
import com.seoultech.livingtogether_android.user.data.source.remote.ProfileRemoteDataSource
import com.seoultech.livingtogether_android.util.AppExecutors
import com.seoultech.livingtogether_android.util.FirebaseUtil

object Injection {

    fun provideNextOfKinRepository(context: Context): NextOfKinRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return NextOfKinRepository.getInstance(
            NextOfKinLocalDataSource.getInstance(AppExecutors(), database.nokDao()))
    }

    fun provideDeviceRepository(context: Context): DeviceRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return DeviceRepository.getInstance(
            DeviceLocalDataSource.getInstance(AppExecutors(), database.deviceDao()), DeviceRemoteDataSource(
                FirebaseUtil.getRemoteDatabase(context).child("device")))
    }

    fun provideSignalRepository(context: Context): SignalRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return SignalRepository.getInstance(SignalLocalDataSource.getInstance(AppExecutors(), database.signalDao()), SignalRemoteDataSource(
            FirebaseUtil.getRemoteDatabase(context).child("signal")))
    }

    fun provideProfileRepository(context: Context): ProfileRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return ProfileRepository.getInstance(
            ProfileLocalDataSource.getInstance(AppExecutors(), database.profileDao()),
            ProfileRemoteDataSource(FirebaseUtil.getRemoteDatabase(context).child("profile")))
    }
}