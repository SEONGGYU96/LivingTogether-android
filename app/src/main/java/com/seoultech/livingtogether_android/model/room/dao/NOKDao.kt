package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity

@Dao
interface NOKDao {
    @Query("SELECT * FROM nok_entity")
    fun getAllObservable(): LiveData<List<NOKEntity>>

    @Insert
    fun insert(nokEntity: NOKEntity)

    @Update
    fun update(nokEntity: NOKEntity)

    @Delete
    fun delete(nokEntity: NOKEntity)
}