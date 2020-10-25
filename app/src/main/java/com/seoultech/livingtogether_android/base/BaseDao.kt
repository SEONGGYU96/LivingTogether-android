package com.seoultech.livingtogether_android.base

import androidx.room.*

interface BaseDao<T> {
    @Insert
    fun insert(entity: T)

    @Insert
    fun insert(vararg obj: T)

    @Update
    fun update(entity: T)

    @Delete
    fun delete(entity: T)
}