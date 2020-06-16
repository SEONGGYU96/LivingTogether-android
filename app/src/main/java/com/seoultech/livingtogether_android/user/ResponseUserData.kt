package com.seoultech.livingtogether_android.user

data class ResponseUserData(
    val status: Int,
    val command: String,
    val data: RequestUserData
)