package com.seoultech.livingtogether_android.user.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.network.RetrofitClient
import com.seoultech.livingtogether_android.user.dao.UserDao
import com.seoultech.livingtogether_android.user.network.RequestUser
import com.seoultech.livingtogether_android.user.model.RequestUserData
import com.seoultech.livingtogether_android.user.model.ResponseUserData
import com.seoultech.livingtogether_android.user.model.UserEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository : BaseRepository<UserEntity>() {

    private val dao: UserDao by lazy { ApplicationImpl.db.userDao() }

    private val observableUser: LiveData<UserEntity> by lazy { dao.getAllObservable() }

    fun getAllObservable() : LiveData<UserEntity> {
        Log.d(TAG, "getAllObservable() : ${observableUser.value}")
        return observableUser
    }

    fun getAll() : UserEntity {
        val value = dao.getAll()
        Log.d(TAG, "getAll() : $value")
        return value
    }

    public override fun insert(entity: UserEntity) {
        super.insert(entity)
        return dao.insert(entity)
    }

    public override fun delete(entity: UserEntity) {
        super.delete(entity)
        return dao.delete(entity)
    }

    public override fun update(entity: UserEntity) {
        super.update(entity)
        return dao.update(entity)
    }


    fun updateServer(userLiveData: LiveData<UserEntity>, callback: UserUpdateCallback) {
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