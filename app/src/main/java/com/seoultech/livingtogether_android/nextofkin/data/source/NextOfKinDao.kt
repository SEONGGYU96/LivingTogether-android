package com.seoultech.livingtogether_android.nextofkin.data.source

import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin

@Dao
abstract class NextOfKinDao : BaseDao<NextOfKin> {

    @Query("SELECT * FROM next_of_kin")
    abstract fun getNextOfKin(): List<NextOfKin>

    @Query("SELECT * FROM next_of_kin WHERE phone_number = :phoneNumber")
    abstract fun getNextOfKinByPhoneNumber(phoneNumber: String): NextOfKin?

    @Query("Delete From next_of_kin")
    abstract fun deleteNextOfKin()

    @Query("DELETE FROM next_of_kin WHERE phone_number = :phoneNumber")
    abstract fun deleteNextOfKinByPhoneNumber(phoneNumber: String)
}