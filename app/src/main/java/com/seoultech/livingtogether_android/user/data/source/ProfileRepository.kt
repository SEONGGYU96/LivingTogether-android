package com.seoultech.livingtogether_android.user.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.network.RetrofitClient
import com.seoultech.livingtogether_android.user.network.RequestUser
import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.user.data.source.local.ProfileDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileRepository {

    companion object {
        private const val TAG = "UserRepository"
    }

    private val dao: ProfileDao by lazy { ApplicationImpl.db.userDao() }

    private val observableUser: LiveData<Profile> by lazy { dao.getAllObservable() }

    fun getAllObservable(): LiveData<Profile> {
        Log.d(TAG, "getAllObservable() : ${observableUser.value}")
        return observableUser
    }

    fun getAll(): Profile {
        val value = dao.getProfile()
        Log.d(TAG, "getAll() : $value")
        return value
    }

    fun insert(entity: Profile) {
        return dao.insert(entity)
    }

    fun delete(entity: Profile) {
        return dao.delete(entity)
    }

    fun update(entity: Profile) {
        return dao.update(entity)
    }

    fun updateServer(userLiveData: LiveData<Profile>, callback: UserUpdateCallback) {
        val requestUser =
            RequestUserData(
                userLiveData.value!!.name,
                userLiveData.value!!.district,
                userLiveData.value!!.address,
                userLiveData.value!!.phoneNum,
                25
            )

        RetrofitClient.create(RequestUser::class.java).requestUpdateUserData(requestUser).enqueue(object :
            Callback<ResponseUserData> {

            override fun onResponse(call: Call<ResponseUserData>, response: Response<ResponseUserData>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "updating to server is success. (${response.body()!!.status})")
                } else {
                    Log.d(TAG, "response is not successful (${response.message()})")
                }
                callback.onResponse(response.isSuccessful)
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d(TAG, "updating to server is failed. \n($t)")
                callback.onFailure(t)
            }
        })
    }

    interface UserUpdateCallback {
        fun onResponse(isSuccessful: Boolean)
        fun onFailure(t: Throwable)
    }
}