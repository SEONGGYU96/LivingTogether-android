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

    var user : LiveData<UserEntity>

    init {
        user = getAll()
    }

    fun getAll(): LiveData<UserEntity> {
        return db.userDao().getAllObservable()
    }

    fun insert(newUser: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.userDao().insert(newUser)
        }
    }

    fun update(newUser: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.userDao().update(newUser)
        }
    }
}