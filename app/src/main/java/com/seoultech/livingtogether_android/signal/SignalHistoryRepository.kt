package com.seoultech.livingtogether_android.signal

import android.util.Log
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl

class SignalHistoryRepository {

    companion object {
        private const val TAG = "SignalHistoryRepository"
    }

    private val dao: SignalHistoryDao by lazy { ApplicationImpl.db.signalHistoryDao() }

    private val observableSignals: LiveData<List<SignalHistoryEntity>> by lazy { dao.getAllObservable() }

    fun getAllObservable(): LiveData<List<SignalHistoryEntity>> {
        Log.d(TAG, "getAllObservable : ${observableSignals.value}")
        return observableSignals
    }

    fun getAllObservable(type: Signal) : LiveData<List<SignalHistoryEntity>> {
        val signalType = when (type) {
            Signal.ACTION -> 1
            Signal.PRESERVE -> 2
            else -> 3
        }
        val result = dao.getAllObservable(signalType)
        Log.d(TAG, "getAllObservable($type) : ${result.value}")
        return result
    }

    fun getAllOfActionSignal() : LiveData<List<SignalHistoryEntity>> {
        val result = dao.getAllOfActionSignal()
        Log.d(TAG, "getAllOfActionSignal : ${result.value}")
        return result
    }

    fun insert(entity: SignalHistoryEntity) {
        dao.insert(entity)
    }

    fun delete(entity: SignalHistoryEntity) {
        dao.delete(entity)
    }

    fun update(entity: SignalHistoryEntity) {
        dao.update(entity)
    }
}