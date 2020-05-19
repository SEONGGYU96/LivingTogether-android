package com.seoultech.livingtogether_android.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.seoultech.livingtogether_android.model.room.entity.UserEntity
import com.seoultech.livingtogether_android.model.room.dao.DeviceDao
import com.seoultech.livingtogether_android.model.room.dao.UserDao
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

@Database(entities = [UserEntity::class, DeviceEntity::class], version = 1)
abstract class DataBaseManager : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun userDao(): UserDao

    companion object {
        private val DB_NAME = "livind-togheher-db"
        private var instance: DataBaseManager? = null

        fun getInstance(context: Context): DataBaseManager {

            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): DataBaseManager {

            return Room.databaseBuilder(context.applicationContext, DataBaseManager::class.java, DB_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).build()
        }
    }

}