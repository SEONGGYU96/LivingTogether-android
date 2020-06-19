package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity
import com.seoultech.livingtogether_android.service.Signal

class ScanTestViewModel(application: Application) : AndroidViewModel(application) {
    private val db = ApplicationImpl.db

    var actionSignalHistory = getActionSignalHistoryAll()

    var preserveSignalHistory = getPreserveSignalHistoryAll()

    fun getActionSignalHistoryAll() : LiveData<List<SignalHistoryEntity>> {
        return db.signalHistoryDao().getAllOfActionSignal()
    }

    fun getPreserveSignalHistoryAll() : LiveData<List<SignalHistoryEntity>> {
        return db.signalHistoryDao().getAllObservable(Signal.PRESERVE)
    }
}