package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity

class ScanTestViewModel(application: Application) : AndroidViewModel(application) {
    private val db = DataBaseManager.getInstance(application)

    var actionSignalHistory = getActionSignalHistoryAll()

    var preserveSignalHistory = getPreserveSignalHistoryAll()

    fun getActionSignalHistoryAll() : LiveData<List<SignalHistoryEntity>> {
        return db.signalHistoryDao().getAllObservable(0)
    }

    fun getPreserveSignalHistoryAll() : LiveData<List<SignalHistoryEntity>> {
        return db.signalHistoryDao().getAllObservable(1)
    }
}