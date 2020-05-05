package com.seoultech.livingtogether_android.data

data class SensorData(
    var name: String?,
    var loction: String?,
    var lastDetectionEvent: Int?,
    var isWorking: Boolean?,
    var kind: String?
)