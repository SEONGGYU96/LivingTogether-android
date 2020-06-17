package com.seoultech.livingtogether_android.user

data class RequestUserData(
    val name: String,
    val location : String,
    val address : String,
    val phone : String,
    val age : Int
)