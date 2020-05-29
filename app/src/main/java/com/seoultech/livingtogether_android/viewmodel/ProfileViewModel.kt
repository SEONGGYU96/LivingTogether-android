package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

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
}