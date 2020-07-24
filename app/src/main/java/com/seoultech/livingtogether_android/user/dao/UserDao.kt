package com.seoultech.livingtogether_android.user.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.user.model.UserEntity

@Dao
abstract class UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM user_entity")
    abstract fun getAllObservable(): LiveData<UserEntity>

    @Query("SELECT * FROM user_entity")
    abstract fun getAll(): UserEntity

    @Query("DELETE FROM user_entity")
    abstract fun deleteAll()
}
