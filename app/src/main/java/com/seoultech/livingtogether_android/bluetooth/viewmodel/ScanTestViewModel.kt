package com.seoultech.livingtogether_android.bluetooth.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.signal.SignalHistoryEntity
import com.seoultech.livingtogether_android.signal.SignalHistoryRepository
import com.seoultech.livingtogether_android.signal.Signal

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
