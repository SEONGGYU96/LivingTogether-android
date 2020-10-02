package com.seoultech.livingtogether_android.user.data.source

import com.seoultech.livingtogether_android.user.data.Profile

interface ProfileDataSource {

    interface GetProfileCallback {

        fun onProfileLoaded(profile: Profile)

        fun onDataNotAvailable()
    }

    fun getProfile(callback: GetProfileCallback)

    fun saveProfile(profile: Profile)
}