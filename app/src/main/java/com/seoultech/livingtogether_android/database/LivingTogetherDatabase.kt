package com.seoultech.livingtogether_android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.device.data.source.local.DeviceDao
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinDao
import com.seoultech.livingtogether_android.signal.data.source.local.SignalDao
import com.seoultech.livingtogether_android.user.data.source.local.ProfileDao
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.signal.data.Signal

@Database(entities = [Profile::class, Device::class, NextOfKin::class, Signal::class], version = 1)
abstract class LivingTogetherDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun profileDao(): ProfileDao
    abstract fun nokDao(): NextOfKinDao
    abstract fun signalDao(): SignalDao

    companion object {
        private const val DB_NAME = "LivingTogether.db"

        private var INSTANCE: LivingTogetherDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): LivingTogetherDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        LivingTogetherDatabase::class.java, DB_NAME
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }
}