package com.seoultech.livingtogether_android.bluetooth.model

import com.seoultech.livingtogether_android.device.data.Device

data class LastByteCode(
    var lastDetectedCode1: Byte = -1,
    var lastDetectedCode2: Byte = 1
)