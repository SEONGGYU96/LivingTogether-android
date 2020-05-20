package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.model.room.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_entity")
    fun getAllObservable(): LiveData<List<UserEntity>>

    @Insert
    fun insert(deviceEntity: UserEntity)

    @Update
    fun update(deviceEntity: UserEntity)

    @Delete
    fun delete(deviceEntity: UserEntity)

    @Query("DELETE FROM user_entity")
    fun deleteAll()
}
