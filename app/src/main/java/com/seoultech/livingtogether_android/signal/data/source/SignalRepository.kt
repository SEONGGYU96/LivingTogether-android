package com.seoultech.livingtogether_android.signal.data.source

import com.seoultech.livingtogether_android.signal.data.Signal

class SignalRepository(
    private val signalLocalDataSource: SignalDataSource,
    private val signalRemoteDataSource: SignalDataSource
) : SignalDataSource {

    override fun getSignals(callback: SignalDataSource.LoadSignalsCallback) {
        signalLocalDataSource.getSignals(object : SignalDataSource.LoadSignalsCallback {
            override fun onSignalsLoaded(signals: List<Signal>) {
                callback.onSignalsLoaded(signals)
            }

            override fun onDataNotAvailable() {
                getSignalsFromRemoteDataSource(callback)
            }
        })
    }

    private fun getSignalsFromRemoteDataSource(callback: SignalDataSource.LoadSignalsCallback) {
        signalRemoteDataSource.getSignals(object: SignalDataSource.LoadSignalsCallback {
            override fun onSignalsLoaded(signals: List<Signal>) {
                refreshLocalDataSource(signals)
                callback.onSignalsLoaded(signals)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshLocalDataSource(signals: List<Signal>) {
        signalLocalDataSource.run {
            deleteAllSignals()
            for (signal in signals) {
                saveSignal(signal)
            }
        }
    }

    override fun deleteAllSignals() {
        signalLocalDataSource.deleteAllSignals()
        signalRemoteDataSource.deleteAllSignals()
    }

    override fun saveSignal(signal: Signal) {
        signalLocalDataSource.saveSignal(signal)
        signalRemoteDataSource.saveSignal(signal)
    }

    companion object {

        private var INSTANCE: SignalRepository? = null

        @JvmStatic fun getInstance(signalLocalDataSource: SignalDataSource, signalRemoteDataSource: SignalDataSource) =
            INSTANCE ?: synchronized(SignalRepository::class.java) {
                INSTANCE ?: SignalRepository(signalLocalDataSource, signalRemoteDataSource)
                    .also { INSTANCE = it }
            }
    }
}