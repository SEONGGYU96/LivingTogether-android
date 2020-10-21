package com.seoultech.livingtogether_android

object Status {

    private const val DEVICE_EXIST = 1
    private const val BLUETOOTH_ON = 2
    private const val CONNECTION_OK = 3

    const val NORMAL = 100
    const val NO_DEVICE = 101
    const val BLUETOOTH_OFF = 102
    const val NO_CONNECTION = 103

    private var status = 0

    fun setBluetoothOn(isOn: Boolean): Int {
        return setValue(isOn, BLUETOOTH_ON) { isBluetoothOn() }
    }

    fun setConnectOk(isOk: Boolean): Int {
        return setValue(isOk, CONNECTION_OK) { isConnectionOk() }
    }

    fun setDeviceExist(isExist: Boolean): Int {
        return setValue(isExist, DEVICE_EXIST) { isDeviceExist() }
    }

    fun getStatus(): Int {
        if (!isDeviceExist()) {
            return NO_DEVICE
        }

        if (!isBluetoothOn()) {
            return BLUETOOTH_OFF
        }

        if (!isConnectionOk()) {
            return NO_CONNECTION
        }

        return NORMAL
    }

    private fun setValue(boolean: Boolean, value: Int, isAlready: () -> Boolean): Int {
        if (boolean) {
            if (!isAlready()) {
                status += (1 shl value)
            }
        } else {
            if (isAlready()) {
                status -= (1 shl value)
            }
        }
        return getStatus()
    }

    private fun checkValue(value: Int): Boolean {
        return status and (1 shl value) > 0
    }

    private fun isBluetoothOn(): Boolean {
        return checkValue(BLUETOOTH_ON)
    }

    private fun isDeviceExist(): Boolean {
        return checkValue(DEVICE_EXIST)
    }

    private fun isConnectionOk(): Boolean {
        return checkValue(CONNECTION_OK)
    }
}