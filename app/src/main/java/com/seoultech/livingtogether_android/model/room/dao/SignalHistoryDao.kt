package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity
import com.seoultech.livingtogether_android.service.Signal

@Dao
abstract class SignalHistoryDao : BaseDao<SignalHistoryEntity> {
    @Query("SELECT * FROM signal_history_entity")
    abstract fun getAll(): List<SignalHistoryEntity>

    @Query("SELECT * FROM signal_history_entity WHERE signal_type = :type")
    abstract fun getAll(type: Signal) : List<SignalHistoryEntity>

    @Query("SELECT * FROM signal_history_entity")
    abstract fun getAllObservable(): LiveData<List<SignalHistoryEntity>>

    @Query("SELECT * FROM signal_history_entity WHERE signal_type = :type")
    abstract fun getAllObservable(type: Signal) : LiveData<List<SignalHistoryEntity>>

    @Query("SELECT * FROM signal_history_entity WHERE signal_type = 'ACTION' OR signal_type = 'RESIST'")
    abstract fun getAllOfActionSignal() : LiveData<List<SignalHistoryEntity>>
}