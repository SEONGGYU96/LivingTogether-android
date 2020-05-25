package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity

class NOKViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "NOKViewModel"
    }

    var noks : LiveData<List<NOKEntity>>

    val db = DataBaseManager.getInstance(application)

    init {
        noks = getAll()

        Log.d(TAG, "noks : ${noks.value}")
    }

    fun getAll(): LiveData<List<NOKEntity>> {
        return db.nokDao().getAllObservable()
    }

}