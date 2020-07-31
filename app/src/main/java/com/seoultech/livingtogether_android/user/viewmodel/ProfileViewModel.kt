package com.seoultech.livingtogether_android.user.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.user.model.UserEntity
import com.seoultech.livingtogether_android.user.repository.UserRepository
import com.seoultech.livingtogether_android.util.SharedPreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {

    private val userRepository: UserRepository by lazy { UserRepository() }
    
    var userLiveData = getObservable()

    val isInitialized = SharedPreferenceManager.getInitializing()

    private fun getObservable(): LiveData<UserEntity> {
        return userRepository.getAllObservable()
    }

    fun update() {
        if (userLiveData.value == null) {
            Log.d(TAG, "userData is null yet.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            userRepository.update(userLiveData.value!!)
        }

        userRepository.updateServer(userLiveData, object: UserRepository.UserUpdateCallback {
            override fun onResponse(isSuccessful: Boolean) {
                if (isSuccessful) {
                    Toast.makeText(getApplication(), "저장되었습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(getApplication(), "알 수 없는 에러가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(t: Throwable) {
                Toast.makeText(getApplication(), "네트워크 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        SharedPreferenceManager.setInitializing(true)

        finishHandler.value = true
    }
}