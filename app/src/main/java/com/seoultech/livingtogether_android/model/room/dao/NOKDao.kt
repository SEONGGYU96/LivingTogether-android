package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity

@Dao
abstract class NOKDao : BaseDao<NOKEntity> {
    @Query("SELECT * FROM nok_entity")
    abstract fun getAllObservable(): LiveData<List<NOKEntity>>

    @Query("Delete From nok_entity")
    abstract fun deleteAll()
}