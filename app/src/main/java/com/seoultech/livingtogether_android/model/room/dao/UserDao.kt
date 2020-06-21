package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.model.room.entity.UserEntity

@Dao
abstract class UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM user_entity")
    abstract fun getAllObservable(): LiveData<UserEntity>

    @Query("DELETE FROM user_entity")
    abstract fun deleteAll()
}
