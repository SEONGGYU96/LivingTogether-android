package com.seoultech.livingtogether_android.nok.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.nok.model.NOKEntity

@Dao
abstract class NOKDao : BaseDao<NOKEntity> {
    @Query("SELECT * FROM nok_entity")
    abstract fun getAllObservable(): LiveData<List<NOKEntity>>

    @Query("Delete From nok_entity")
    abstract fun deleteAll()
}