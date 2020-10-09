package com.seoultech.livingtogether_android.device.data.source.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.source.DeviceDataSource

class DeviceRemoteDataSource(private val deviceDatabase: DatabaseReference) : DeviceDataSource {

    override fun getDevices(callback: DeviceDataSource.LoadDevicesCallback) {
        deviceDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callback.onDataNotAvailable()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    callback.onDataNotAvailable()
                    return
                }
                val result = mutableListOf<Device>()
                for (_snapshot in snapshot.children) {
                    _snapshot.getValue<Device>()?.let { result.add(it) }
                }
                callback.onDevicesLoaded(result)
            }
        })
    }

    override fun getDevice(deviceAddress: String, callback: DeviceDataSource.GetDeviceCallback) {
        deviceDatabase.child(deviceAddress).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    callback.onDataNotAvailable()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = snapshot.getValue<Device>()
                    if (result != null) {
                        callback.onDeviceLoaded(result)
                    } else {
                        callback.onDataNotAvailable()
                    }
                }
            })
    }

    override fun getDeviceAddresses(callback: DeviceDataSource.LoadDeviceAddressesCallback) {
        deviceDatabase.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callback.onDataNotAvailable()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    callback.onDataNotAvailable()
                    return
                }
                val result = mutableListOf<String>()
                for (_snapshot in snapshot.children) {
                    _snapshot.getValue<Device>()?.let { result.add(it.deviceAddress) }
                }
                callback.onDeviceAddressesLoaded(result)
            }
        })
    }

    override fun getLatestDevice(callback: DeviceDataSource.GetDeviceCallback) {
        deviceDatabase.orderByChild("initDate").limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callback.onDataNotAvailable()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue<Device>()
                if (result != null) {
                    callback.onDeviceLoaded(result)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        })
    }

    override fun saveDevice(device: Device) {
        deviceDatabase.child(device.deviceAddress).setValue(device)
    }

    override fun deleteDevice(deviceAddress: String) {
        deviceDatabase.child(deviceAddress).removeValue()
    }

    override fun deleteAllDevices() {
        deviceDatabase.removeValue()
    }

    override fun updateDevice(device: Device) {
        saveDevice(device)
    }
}