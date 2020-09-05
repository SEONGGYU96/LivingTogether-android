package com.seoultech.livingtogether_android.nextofkin.data.source

import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.util.AppExecutors

class NextOfKinLocalDataSource private constructor(
    private val appExecutors: AppExecutors, private val nextOfKinDao: NextOfKinDao) : NextOfKinDataSource {

    override fun getNextOfKin(callback: NextOfKinDataSource.LoadNextOfKinCallback) {
        appExecutors.diskIO.execute {
            val nextOfKin = nextOfKinDao.getNextOfKin()
            appExecutors.mainThread.execute {
                if (nextOfKin.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onNextOfKinLoaded(nextOfKin)
                }
            }
        }
    }

    override fun getNextOfKin(phoneNumber: String, callback: NextOfKinDataSource.GetNextOfKinCallback) {
        appExecutors.diskIO.execute {
            val nextOfKin = nextOfKinDao.getNextOfKinByPhoneNumber(phoneNumber)
            appExecutors.mainThread.execute {
                if (nextOfKin == null) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onNextOfKinLoaded(nextOfKin)
                }
            }
        }
    }

    override fun saveNextOfKin(nextOfKin: NextOfKin) {
        appExecutors.diskIO.execute {
            nextOfKinDao.insert(nextOfKin)
        }
    }

    override fun deleteNextOfKin(phoneNumber: String) {
        appExecutors.diskIO.execute {
            nextOfKinDao.deleteNextOfKinByPhoneNumber(phoneNumber)
        }
    }

    companion object {
        private var INSTANCE: NextOfKinLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, nextOfKinDao: NextOfKinDao): NextOfKinLocalDataSource {
            if (INSTANCE == null) {
                synchronized(NextOfKinLocalDataSource::javaClass) {
                    INSTANCE = NextOfKinLocalDataSource(appExecutors, nextOfKinDao)
                }
            }
            return INSTANCE!!
        }
    }
}