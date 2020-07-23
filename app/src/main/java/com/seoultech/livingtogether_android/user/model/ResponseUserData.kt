package com.seoultech.livingtogether_android.user.model

import com.seoultech.livingtogether_android.user.model.RequestUserData

data class ResponseUserData(
    val status: Int,
    val command: String,
    val data: RequestUserData
)