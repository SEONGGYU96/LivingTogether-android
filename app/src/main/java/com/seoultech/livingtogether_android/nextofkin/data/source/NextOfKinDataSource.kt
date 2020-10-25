package com.seoultech.livingtogether_android.nextofkin.data.source

import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin

interface NextOfKinDataSource {

    interface LoadNextOfKinCallback {

        fun onNextOfKinLoaded(nextOfKin: List<NextOfKin>)

        fun onDataNotAvailable()
    }

    interface GetNextOfKinCallback {

        fun onNextOfKinLoaded(nextOfKin: NextOfKin)

        fun onDataNotAvailable()
    }

    fun getNextOfKin(callback: LoadNextOfKinCallback)

    fun getNextOfKin(phoneNumber: String, callback: GetNextOfKinCallback)

    fun saveNextOfKin(nextOfKin: NextOfKin)

    fun deleteNextOfKin(phoneNumber: String)
}