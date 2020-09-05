package com.seoultech.livingtogether_android

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.bluetooth.viewmodel.ScanViewModel
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository
import com.seoultech.livingtogether_android.device.viewmodel.DeviceViewModel
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinRepository
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.signal.SignalHistoryRepository

class ViewModelFactory private constructor(
    private val nextOfKinRepository: NextOfKinRepository,
    private val deviceRepository: DeviceRepository,
    private val signalHistoryRepository: SignalHistoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NextOfKinViewModel::class.java) ->
                    NextOfKinViewModel(nextOfKinRepository)
                isAssignableFrom(DeviceViewModel::class.java) ->
                    DeviceViewModel(deviceRepository)
                isAssignableFrom(ScanViewModel::class.java) ->
                    ScanViewModel(deviceRepository, signalHistoryRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideNextOfKinRepository(application.applicationContext),
                    Injection.provideDeviceRepository(application.applicationContext),
                    Injection.provideSignalHistoryRepository(application.applicationContext))
                    .also { INSTANCE = it }
            }
    }
}