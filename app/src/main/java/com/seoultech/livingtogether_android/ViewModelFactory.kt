package com.seoultech.livingtogether_android

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.bluetooth.viewmodel.ScanViewModel
import com.seoultech.livingtogether_android.contacts.ContactViewModel
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository
import com.seoultech.livingtogether_android.device.viewmodel.DeviceViewModel
import com.seoultech.livingtogether_android.device.viewmodel.LocationViewModel
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinRepository
import com.seoultech.livingtogether_android.nextofkin.viewmodel.AddNextOfKinViewModel
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.signal.data.source.SignalRepository
import com.seoultech.livingtogether_android.user.data.source.ProfileRepository
import com.seoultech.livingtogether_android.user.viewmodel.ProfileViewModel
import com.seoultech.livingtogether_android.viewmodel.MainViewModel

class ViewModelFactory private constructor(
    private val nextOfKinRepository: NextOfKinRepository,
    private val deviceRepository: DeviceRepository,
    private val signalRepository: SignalRepository,
    private val profileRepository: ProfileRepository,
    private val bluetoothAdapter: BluetoothAdapter,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NextOfKinViewModel::class.java) ->
                    NextOfKinViewModel(nextOfKinRepository)
                isAssignableFrom(DeviceViewModel::class.java) ->
                    DeviceViewModel(deviceRepository)
                isAssignableFrom(ScanViewModel::class.java) ->
                    ScanViewModel(deviceRepository, signalRepository)
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(bluetoothAdapter, application)
                isAssignableFrom(AddNextOfKinViewModel::class.java) ->
                    AddNextOfKinViewModel(nextOfKinRepository)
                isAssignableFrom(ContactViewModel::class.java) ->
                    ContactViewModel(application)
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(profileRepository)
                isAssignableFrom(LocationViewModel::class.java) ->
                    LocationViewModel(deviceRepository)
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
                    Injection.provideSignalRepository(application.applicationContext),
                    Injection.provideProfileRepository(application.applicationContext),
                    (application.applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter,
                    application)
                    .also { INSTANCE = it }
            }
    }
}