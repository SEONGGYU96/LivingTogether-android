package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity

@Dao
interface SignalHistoryDao {
    @Query("SELECT * FROM signal_history_entity")
    fun getAll(): List<SignalHistoryEntity>

    @Query("SELECT * FROM signal_history_entity WHERE signal_type = :type")
    fun getAll(type: Int) : List<SignalHistoryEntity>

    @Query("SELECT * FROM signal_history_entity")
    fun getAllObservable(): LiveData<List<SignalHistoryEntity>>

    @Query("SELECT * FROM signal_history_entity WHERE signal_type = :type")
    fun getAllObservable(type: Int) : LiveData<List<SignalHistoryEntity>>

    @Insert
    fun insert(signalHistoryEntity: SignalHistoryEntity)

    @Update
    fun update(signalHistoryEntity: SignalHistoryEntity)

    @Delete
    fun delete(signalHistoryEntity: SignalHistoryEntity)
}