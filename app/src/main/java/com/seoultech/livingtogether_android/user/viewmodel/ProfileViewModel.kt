package com.seoultech.livingtogether_android.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.user.data.Profile
import com.seoultech.livingtogether_android.user.data.source.ProfileDataSource
import com.seoultech.livingtogether_android.user.data.source.ProfileRepository
import com.seoultech.livingtogether_android.util.FirebaseUtil
import com.seoultech.livingtogether_android.util.StringUtil
import java.lang.StringBuilder
import java.util.*

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    var profileId: String? = null

    var name = MutableLiveData<String>()

    var showingAddress = MutableLiveData<String>()

    var cityOfAddress = MutableLiveData<String>()

    var fullAddress = MutableLiveData<String>()

    var extraAddress = MutableLiveData<String>()

    var phoneNumber = MutableLiveData<String>()

    private var isNew = true

    private var cacheJoin = ""

    private val _saveProfileEvent = MutableLiveData<Boolean>()
    val saveProfileEvent: LiveData<Boolean>
        get() = _saveProfileEvent

    private val _isWrongData = MutableLiveData<Boolean>()
    val isWrongData: LiveData<Boolean>
        get() = _isWrongData

    private val _searchAddressEvent = MutableLiveData<Boolean>()
    val searchAddressEvent: LiveData<Boolean>
        get() = _searchAddressEvent

    fun getProfile(isEdit: Boolean) {
        profileRepository.getProfile(object : ProfileDataSource.GetProfileCallback {
            override fun onProfileLoaded(profile: Profile) {
                name.value = profile.name
                cityOfAddress.value = profile.city
                fullAddress.value = profile.fullAddress
                extraAddress.value = profile.extraAddress
                showingAddress.value = generateShowingAddress(profile, isEdit)
                phoneNumber.value = profile.phoneNumber
                profileId = profile.id
                cacheJoin = profile.join
                isNew = false
            }

            override fun onDataNotAvailable() {
                isNew = true
            }
        })
    }

    private fun generateShowingAddress(profile: Profile, isEdit: Boolean): String {
        val result = StringBuilder(profile.fullAddress)
        if (!isEdit) {
            result.append(" ").append(profile.extraAddress)
        }
        return result.toString()
    }

    fun searchAddress() {
        _searchAddressEvent.value = true
    }

    fun setAddress(city: String, fullAddress: String) {
        this.cityOfAddress.value = city
        this.fullAddress.value = fullAddress
        this.showingAddress.value = fullAddress
    }

    fun saveProfile() {
        val currentName = name.value
        val currentCity = cityOfAddress.value
        val currentMiddleAddress = fullAddress.value
        val currentExtraAddress = extraAddress.value
        val currentPhoneNumber = phoneNumber.value

        if (currentName == null ||currentCity == null || currentMiddleAddress == null
            || currentExtraAddress == null || currentPhoneNumber == null) {
            _isWrongData.value = true
            return
        }

        if (isNew || profileId == null) {
            createProfile(Profile(currentName, currentCity,
                currentMiddleAddress, currentExtraAddress, currentPhoneNumber,
                StringUtil.getCurrentTime(year = true, longYear = true, time = false)))
            FirebaseUtil.pushEmergency(true)
        } else {
            updateProfile(Profile(currentName, currentCity,
                currentMiddleAddress, currentExtraAddress, currentPhoneNumber, cacheJoin, profileId!!))
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