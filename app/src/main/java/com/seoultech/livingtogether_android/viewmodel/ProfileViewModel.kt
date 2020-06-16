package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.UserEntity
import com.seoultech.livingtogether_android.network.RetrofitClient
import com.seoultech.livingtogether_android.user.RequestUser
import com.seoultech.livingtogether_android.user.RequestUserData
import com.seoultech.livingtogether_android.user.ResponseUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val db = DataBaseManager.getInstance(application)

    var userLiveData: LiveData<UserEntity>

    lateinit var user: UserEntity

    init {
        userLiveData = this.getObservable()

        viewModelScope.launch(Dispatchers.IO) {
            user = db.userDao().getAll()
        }
    }

    fun getObservable(): LiveData<UserEntity> {
        return db.userDao().getAllObservable()
    }

    fun update(newUser: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.userDao().update(newUser)
        }
    }

    //Todo: request 객체 정리하기. 필드 맞추기
    fun updateServer() {
        val requestUser = RequestUserData(user.name, user.address, user.address, user.phoneNum, 25)
        RetrofitClient.create(RequestUser::class.java).requestUpdateUserData(requestUser).enqueue(object : Callback<ResponseUserData> {

            override fun onResponse(call: Call<ResponseUserData>, response: Response<ResponseUserData>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "updating to server is success. (${response.body()!!.status})")
                    Toast.makeText(getApplication(), "저장되었습니다.", Toast.LENGTH_SHORT).show()

                } else {
                    Log.d(TAG, "response is not successful (${response.message()})")
                    Toast.makeText(getApplication(), "알 수 없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseUserData>, t: Throwable) {
                Log.d(TAG, "updating to server is failed. \n($t)")
                Toast.makeText(getApplication(), "네트워크 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}