package com.seoultech.livingtogether_android.user.data.source.local

import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.user.data.source.ProfileDataSource
import com.seoultech.livingtogether_android.util.AppExecutors

class ProfileLocalDataSource private constructor(
    private val appExecutors: AppExecutors, private val profileDao: ProfileDao
): ProfileDataSource {

    override fun getProfile(callback: ProfileDataSource.GetProfileCallback) {
        appExecutors.diskIO.execute {
            val profile = profileDao.getProfile()
            appExecutors.mainThread.execute {
                if (profile == null) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onProfileLoaded(profile)
                }
            }
        }
    }

    override fun saveProfile(profile: Profile) {
        appExecutors.diskIO.execute {
            profileDao.insert(profile)
        }
    }

    companion object {
        private var INSTANCE: ProfileLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, profileDao: ProfileDao): ProfileLocalDataSource {
            if (INSTANCE == null) {
                synchronized(ProfileLocalDataSource::javaClass) {
                    INSTANCE =
                        ProfileLocalDataSource(
                            appExecutors,
                            profileDao
                        )
                }
            }
            return INSTANCE!!
        }
    }
}