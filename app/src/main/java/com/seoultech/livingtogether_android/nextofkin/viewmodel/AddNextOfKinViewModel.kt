package com.seoultech.livingtogether_android.nextofkin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinRepository
import java.lang.StringBuilder

class AddNextOfKinViewModel(private val nextOfKinRepository: NextOfKinRepository) : ViewModel() {

    val name = MutableLiveData<String>()

    val phoneNumber = MutableLiveData<String>()

    private val _nextOfKinUpdated = MutableLiveData<Boolean>()
    val nextOfKinUpdated : LiveData<Boolean>
        get() = _nextOfKinUpdated

    val isNameNotEmpty : LiveData<Boolean> = Transformations.map(name) {
        it.isNotBlank()
    }

    val isPhoneNumberNotEmpty : LiveData<Boolean> = Transformations.map(phoneNumber) {
        it.isNotBlank()
    }

    fun addNewNextOfKin() {
        nextOfKinRepository.saveNextOfKin(NextOfKin(name.value!!, formattingPhoneNumber(phoneNumber.value!!)))
        _nextOfKinUpdated.value = true
    }

    private fun formattingPhoneNumber(phoneNumber: String): String {
        phoneNumber.run {
            replace(" ", "")

            when (length) {
                11 -> {
                    return StringBuilder()
                        .append(substring(0, 3)).append("-")
                        .append(substring(3, 7)).append("-")
                        .append(substring(7)).toString()
                }
                9 -> {
                    return StringBuilder()
                        .append(substring(0, 2)).append("-")
                        .append(substring(2, 5)).append("-")
                        .append(substring(5))
                        .toString()
                }
                10 -> {
                    return StringBuilder()
                        .append(substring(0, 3)).append("-")
                        .append(substring(3, 6)).append("-")
                        .append(substring(6))
                        .toString()
                }
                else -> return this
            }
        }
    }
}