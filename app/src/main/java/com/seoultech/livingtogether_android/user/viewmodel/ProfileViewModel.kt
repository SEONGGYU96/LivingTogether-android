package com.seoultech.livingtogether_android.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.user.data.source.ProfileDataSource
import com.seoultech.livingtogether_android.user.data.source.ProfileRepository
import java.lang.StringBuilder

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    var profileId: String? = null

    var name = MutableLiveData<String>()

    var showingAddress = MutableLiveData<String>()

    var provinceOfAddress = MutableLiveData<String>()

    var cityOfAddress = MutableLiveData<String>()

    var middleAddress = MutableLiveData<String>()

    var extraAddress = MutableLiveData<String>()

    var phoneNumber = MutableLiveData<String>()

    private var isNew = true

    private val _saveProfileEvent = MutableLiveData<Boolean>()
    val saveProfileEvent: LiveData<Boolean>
        get() = _saveProfileEvent

    private val _isWrongData = MutableLiveData<Boolean>()
    val isWrongData: LiveData<Boolean>
        get() = _isWrongData

    fun getProfile(isEdit: Boolean) {
        profileRepository.getProfile(object : ProfileDataSource.GetProfileCallback {
            override fun onProfileLoaded(profile: Profile) {
                name.value = profile.name
                provinceOfAddress.value = profile.province
                cityOfAddress.value = profile.city
                middleAddress.value = profile.middleAddress
                extraAddress.value = profile.extraAddress
                showingAddress.value = generateShowingAddress(profile, isEdit)
                phoneNumber.value = profile.phoneNumber
                profileId = profile.id
                isNew = false
            }

            override fun onDataNotAvailable() {
                isNew = false
            }
        })
    }

    private fun generateShowingAddress(profile: Profile, isEditMode: Boolean): String {
        val result = StringBuilder(profile.province)
            .append(" ")
            .append(profile.city)
            .append(" ")
            .append(profile.middleAddress)
            .append(" ")

        if (!isEditMode) {
            result
            .append(profile.extraAddress)
        }

        return result.toString()
    }

    fun saveProfile() {
        val currentName = name.value
        val currentProvince = provinceOfAddress.value
        val currentCity = cityOfAddress.value
        val currentMiddleAddress = middleAddress.value
        val currentExtraAddress = extraAddress.value
        val currentPhoneNumber = phoneNumber.value

        if (currentName == null || currentProvince == null ||currentCity == null || currentMiddleAddress == null
            || currentExtraAddress == null || currentPhoneNumber == null) {
            _isWrongData.value = true
            return
        }

        if (isNew || profileId == null) {
            createProfile(Profile(currentName, currentProvince, currentCity,
                currentMiddleAddress, currentExtraAddress, currentPhoneNumber))
        } else {
            updateProfile(Profile(currentName, currentProvince, currentCity,
                currentMiddleAddress, currentExtraAddress, currentPhoneNumber, profileId!!))
        }

        _saveProfileEvent.value = true
    }

    private fun updateProfile(profile: Profile) {
        profileRepository.updateProfile(profile)
    }

    private fun createProfile(profile: Profile) {
        profileRepository.saveProfile(profile)
    }
}