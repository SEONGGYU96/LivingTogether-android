package com.seoultech.livingtogether_android.user

import retrofit2.http.Body
import retrofit2.http.PUT

interface RequestUser {

    @PUT("/ltdatainsert")
    fun requestUpdateUserData(@Body userData: RequestUserData)
}