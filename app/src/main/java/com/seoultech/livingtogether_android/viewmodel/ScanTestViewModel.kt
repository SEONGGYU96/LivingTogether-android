package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity
import com.seoultech.livingtogether_android.model.room.repository.SignalHistoryRepository
import com.seoultech.livingtogether_android.service.Signal

class ScanTestViewModel(application: Application) : BaseViewModel(application) {

    private val signalHistoryRepository: SignalHistoryRepository by lazy { SignalHistoryRepository() }

    var actionSignalHistory = getActionSignalHistoryAll()

    var preserveSignalHistory = getPreserveSignalHistoryAll()

    private fun getActionSignalHistoryAll() : LiveData<List<SignalHistoryEntity>> {
        return signalHistoryRepository.getAllOfActionSignal()
    }

    private fun getPreserveSignalHistoryAll() : LiveData<List<SignalHistoryEntity>> {
        return signalHistoryRepository.getAllObservable(Signal.PRESERVE)
    }
}
