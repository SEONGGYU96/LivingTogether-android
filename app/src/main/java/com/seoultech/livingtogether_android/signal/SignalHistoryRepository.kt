package com.seoultech.livingtogether_android.signal

import android.util.Log
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.base.BaseRepository

class SignalHistoryRepository : BaseRepository<SignalHistoryEntity>() {

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

    public override fun insert(entity: SignalHistoryEntity) {
        super.insert(entity)
        dao.insert(entity)
    }

    public override fun delete(entity: SignalHistoryEntity) {
        super.delete(entity)
        dao.delete(entity)
    }

    public override fun update(entity: SignalHistoryEntity) {
        super.update(entity)
        dao.update(entity)
    }
}