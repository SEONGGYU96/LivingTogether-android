package com.seoultech.livingtogether_android.user.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.user.data.Profile

class ProfileViewModel : ViewModel() {

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    private val _profile = MutableLiveData<Profile>()
}