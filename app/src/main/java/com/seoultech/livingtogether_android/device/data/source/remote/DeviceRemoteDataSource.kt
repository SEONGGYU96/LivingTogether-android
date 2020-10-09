package com.seoultech.livingtogether_android.device.data.source.remote

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.source.DeviceDataSource
import com.seoultech.livingtogether_android.signal.data.Signal

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
        deviceDatabase.parent?.child("signal")?.orderByChild("deviceAddress")?.equalTo(deviceAddress)?.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                return
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    return
                }
                for (_snapshot in snapshot.children) {
                    val signal = _snapshot.getValue<Signal>()
                    signal?.let {
                        deviceDatabase.parent?.child("signal")?.child(it.detectiveTime.toString())?.removeValue()
                        Log.d("", "")
                    }
                }
            }
        })
    }

    override fun deleteAllDevices() {
        deviceDatabase.removeValue()
    }

    override fun updateDevice(device: Device) {
        saveDevice(device)
    }
}