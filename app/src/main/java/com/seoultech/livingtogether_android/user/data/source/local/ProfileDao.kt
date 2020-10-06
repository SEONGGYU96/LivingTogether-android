package com.seoultech.livingtogether_android.user.data.source.local

import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.user.data.Profile

@Dao
abstract class ProfileDao : BaseDao<Profile> {
    @Query("SELECT * FROM profile")
    abstract fun getProfile(): Profile?

    @Query("DELETE FROM profile")
    abstract fun deleteAll()
}
