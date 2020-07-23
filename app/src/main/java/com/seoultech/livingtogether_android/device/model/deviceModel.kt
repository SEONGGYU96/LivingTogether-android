package com.seoultech.livingtogether_android.device.model

data class deviceModel(
    var name: String?,
    var loction: String?,
    var lastDetectionEvent: Int?,
    var isWorking: Boolean?,
    var kind: String?
)