package com.seoultech.livingtogether_android.user.network

import com.seoultech.livingtogether_android.user.model.RequestUserData
import com.seoultech.livingtogether_android.user.model.ResponseUserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestUser {

    @POST("/ltdatainsert")
    fun requestUpdateUserData(@Body userData: RequestUserData) : Call<ResponseUserData>
}