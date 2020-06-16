package com.seoultech.livingtogether_android.user

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface RequestUser {

    @POST("/ltdatainsert")
    fun requestUpdateUserData(@Body userData: RequestUserData) : Call<ResponseUserData>
}