package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.model.room.entity.UserEntity
import com.seoultech.livingtogether_android.model.room.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {

    private val userRepository: UserRepository by lazy { UserRepository(application) }
    
    var userLiveData = getObservable()

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

        userRepository.updateServer(userLiveData)

        finishHandler.value = true
    }
}