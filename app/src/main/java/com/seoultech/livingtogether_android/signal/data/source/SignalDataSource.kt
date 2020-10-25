package com.seoultech.livingtogether_android.signal.data.source

import com.seoultech.livingtogether_android.signal.data.Signal


interface SignalDataSource {

    interface LoadSignalsCallback {

        fun onSignalsLoaded(signals: List<Signal>)

        fun onDataNotAvailable()
    }

    fun getSignals(callback: LoadSignalsCallback)

    fun saveSignal(signal: Signal)

    fun deleteAllSignals()
}