package com.seoultech.livingtogether_android.signal.data.source.local

import com.seoultech.livingtogether_android.signal.data.Signal
import com.seoultech.livingtogether_android.signal.data.source.SignalDataSource
import com.seoultech.livingtogether_android.util.AppExecutors

class SignalLocalDataSource(
    private val appExecutors: AppExecutors,
    private val signalDao: SignalDao
): SignalDataSource {

    override fun getSignals(callback: SignalDataSource.LoadSignalsCallback) {
        appExecutors.diskIO.execute {
            val signals = signalDao.getSignals()
            appExecutors.mainThread.execute {
                if (signals.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveSignal(signal: Signal) {
        appExecutors.diskIO.execute {
            signalDao.insert(signal)
        }
    }

    override fun deleteAllSignals() {
        appExecutors.diskIO.execute {
            signalDao.deleteAllSignals()
        }
    }

    companion object {
        private var INSTANCE: SignalLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, signalDao: SignalDao): SignalLocalDataSource {
            if (INSTANCE == null) {
                synchronized(SignalLocalDataSource::javaClass) {
                    INSTANCE =
                        SignalLocalDataSource(appExecutors, signalDao)
                }
            }
            return INSTANCE!!
        }
    }
}