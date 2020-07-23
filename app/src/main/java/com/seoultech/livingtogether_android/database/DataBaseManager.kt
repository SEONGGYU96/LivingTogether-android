package com.seoultech.livingtogether_android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.seoultech.livingtogether_android.database.util.Converters
import com.seoultech.livingtogether_android.user.model.UserEntity
import com.seoultech.livingtogether_android.device.dao.DeviceDao
import com.seoultech.livingtogether_android.nok.dao.NOKDao
import com.seoultech.livingtogether_android.signal.SignalHistoryDao
import com.seoultech.livingtogether_android.user.dao.UserDao
import com.seoultech.livingtogether_android.device.model.DeviceEntity
import com.seoultech.livingtogether_android.nok.model.NOKEntity
import com.seoultech.livingtogether_android.signal.SignalHistoryEntity
import com.seoultech.livingtogether_android.database.util.ioThread

@Database(entities = [UserEntity::class, DeviceEntity::class, NOKEntity::class, SignalHistoryEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class DataBaseManager : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun userDao(): UserDao
    abstract fun nokDao(): NOKDao
    abstract fun signalHistoryDao(): SignalHistoryDao

    companion object {
        private val DB_NAME = "livind-togheher-db"
        private var instance: DataBaseManager? = null

        fun getInstance(context: Context): DataBaseManager {

            return instance
                ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    )
            }
        }



        fun buildDatabase(context: Context): DataBaseManager {

            return Room.databaseBuilder(context.applicationContext, DataBaseManager::class.java,
                DB_NAME
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        //최초 실행 시 User 데이터를 하나 생성
                        ioThread {
                            getInstance(
                                context
                            ).userDao().insert(UserEntity())
                        }
                    }
                })
                //Todo: MainThread 허용하지 않도록 변경.
                .allowMainThreadQueries()
                .build()
        }
    }
}