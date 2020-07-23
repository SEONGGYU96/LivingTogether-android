package com.seoultech.livingtogether_android.user.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.base.BaseRepository
import com.seoultech.livingtogether_android.network.RetrofitClient
import com.seoultech.livingtogether_android.user.dao.UserDao
import com.seoultech.livingtogether_android.user.network.RequestUser
import com.seoultech.livingtogether_android.user.model.RequestUserData
import com.seoultech.livingtogether_android.user.model.ResponseUserData
import com.seoultech.livingtogether_android.user.model.UserEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(val application: Application) : BaseRepository<UserEntity>() {

    private val dao: UserDao by lazy { ApplicationImpl.db.userDao() }

    private val observableUser: LiveData<UserEntity> by lazy { dao.getAllObservable() }

    fun getAllObservable() : LiveData<UserEntity> {
        Log.d(TAG, "getAll() : ${observableUser.value}")
        return observableUser
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


    fun updateServer(userLiveData: LiveData<UserEntity>) {
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
                    Toast.makeText(application, "저장되었습니다.", Toast.LENGTH_SHORT).show()

                } else {
                    Log.d(TAG, "response is not successful (${response.message()})")
                    Toast.makeText(application, "알 수 없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d(TAG, "updating to server is failed. \n($t)")
                Toast.makeText(application, "네트워크 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}