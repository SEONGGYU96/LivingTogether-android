package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NOKViewModel(application: Application) : BaseViewModel(application) {

    var noks = getAll()

    var newNok = NOKEntity()

    fun getAll(): LiveData<List<NOKEntity>> {
        return db.nokDao().getAllObservable()
    }

    fun insert() {
        viewModelScope.launch(Dispatchers.IO) {
            db.nokDao().insert(newNok)
        }
        finishHandler.value = true
    }
}