package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.seoultech.livingtogether_android.model.room.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_entity")
    fun getAllObservable(): List<UserEntity>

    @Insert
    fun insert(userEntity: UserEntity)

    @Update
    fun update(userEntity: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)
  
    @Query("DELETE FROM user_entity")
    fun deleteAll()
}
