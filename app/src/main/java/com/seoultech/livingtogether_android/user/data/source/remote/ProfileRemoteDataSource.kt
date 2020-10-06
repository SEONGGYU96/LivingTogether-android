package com.seoultech.livingtogether_android.user.data.source.remote

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.user.data.source.ProfileDataSource

class ProfileRemoteDataSource(private val profileDatabase: DatabaseReference) : ProfileDataSource {

    companion object {
        private const val TAG = "ProfileRemoteDataSource"
    }

    override fun getProfile(callback: ProfileDataSource.GetProfileCallback) {
        profileDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "getProfile:onCancelled", error.toException())
                callback.onDataNotAvailable()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val result = snapshot.getValue<Profile>()
                if (result == null) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onProfileLoaded(result)
                }
            }
        })
    }

    override fun saveProfile(profile: Profile) {
        profileDatabase.setValue(profile)
    }

    override fun updateProfile(profile: Profile) {
        profileDatabase.setValue(profile)
    }

    override fun deleteProfile() {
        profileDatabase.setValue(null)
    }
}