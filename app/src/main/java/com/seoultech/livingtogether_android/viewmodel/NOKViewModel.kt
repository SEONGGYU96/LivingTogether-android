package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NOKViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "NOKViewModel"
    }

    private val db = DataBaseManager.getInstance(application)

    var noks : LiveData<List<NOKEntity>>

    var newNok = NOKEntity()

    init {
        noks = getAll()
    }

    fun getAll(): LiveData<List<NOKEntity>> {
        return db.nokDao().getAllObservable()
    }

    fun insert(newNok:NOKEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            db.nokDao().insert(newNok)
        }
    }
}