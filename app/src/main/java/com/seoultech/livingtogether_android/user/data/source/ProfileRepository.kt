package com.seoultech.livingtogether_android.user.data.source

import com.seoultech.livingtogether_android.user.data.Profile

class ProfileRepository(
    val profileLocalDataSource: ProfileDataSource,
    val profileRemoteDataSource: ProfileDataSource
) : ProfileDataSource {

    private var cachedProfile: Profile? = null

    private var cacheIsDirty = false

    override fun getProfile(callback: ProfileDataSource.GetProfileCallback) {
        if (cachedProfile != null && !cacheIsDirty) {
            callback.onProfileLoaded(cachedProfile!!)
            return
        }

        if (cacheIsDirty) {
            getProfileFromRemoteDataSource(callback)
            return
        }

        profileLocalDataSource.getProfile(object : ProfileDataSource.GetProfileCallback {
            override fun onProfileLoaded(profile: Profile) {
                refreshCache(profile)
                callback.onProfileLoaded(profile)
            }

            override fun onDataNotAvailable() {
                getProfileFromRemoteDataSource(callback)
            }
        })
    }

    override fun saveProfile(profile: Profile) {
        cacheAndPerform(profile) {
            profileLocalDataSource.saveProfile(it)
            profileRemoteDataSource.saveProfile(it)
        }
    }

    override fun updateProfile(profile: Profile) {
        cacheAndPerform(profile) {
            profileLocalDataSource.updateProfile(it)
            profileRemoteDataSource.updateProfile(it)
        }
    }

    override fun deleteProfile() {
        profileLocalDataSource.deleteProfile()
        profileRemoteDataSource.deleteProfile()
        cachedProfile = null
    }

    fun getProfileFromRemoteDataSource(callback: ProfileDataSource.GetProfileCallback) {
        profileRemoteDataSource.getProfile(object : ProfileDataSource.GetProfileCallback {
            override fun onProfileLoaded(profile: Profile) {
                refreshCache(profile)
                refreshLocalDataSource(profile)
                callback.onProfileLoaded(profile)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshLocalDataSource(profile: Profile) {
        profileLocalDataSource.run {
            deleteProfile()
            saveProfile(profile)
        }
    }

    private fun refreshCache(profile: Profile) {
        cacheAndPerform(profile) {}
        cacheIsDirty = false
    }

    private inline fun cacheAndPerform(profile: Profile, perform: (Profile) -> Unit) {
        cachedProfile = profile
        perform(profile)
    }

    companion object {

        private var INSTANCE: ProfileRepository? = null

        @JvmStatic fun getInstance(profileLocalDataSource: ProfileDataSource,
                                   profileRemoteDataSource: ProfileDataSource) =
            INSTANCE ?: synchronized(ProfileRepository::class.java) {
                INSTANCE ?: ProfileRepository(profileLocalDataSource, profileRemoteDataSource)
                    .also { INSTANCE = it }
            }
    }
}