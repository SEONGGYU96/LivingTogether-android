package com.seoultech.livingtogether_android.signal.data.source.local

import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.signal.data.Signal

@Dao
abstract class SignalDao : BaseDao<Signal> {
    @Query("SELECT * FROM signal")
    abstract fun getSignals(): List<Signal>

    @Query("DELETE FROM signal")
    abstract fun deleteAllSignals()
}