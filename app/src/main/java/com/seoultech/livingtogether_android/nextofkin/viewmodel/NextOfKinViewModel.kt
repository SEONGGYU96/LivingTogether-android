package com.seoultech.livingtogether_android.nextofkin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinDataSource
import com.seoultech.livingtogether_android.nextofkin.data.source.NextOfKinRepository

class NextOfKinViewModel(private val nextOfKinRepository: NextOfKinRepository) : ViewModel() {

    private val _items = MutableLiveData<List<NextOfKin>>().apply { value = emptyList() }
    val items: LiveData<List<NextOfKin>>
        get() = _items

    private val _emptyListEvent = MutableLiveData<Boolean>()
    val emptyListEvent: LiveData<Boolean>
        get() = _emptyListEvent

    val deleteCache = mutableListOf<String>()

    private val _newNextOfKinEvent = MutableLiveData<Boolean>()
    val newNextOfKinEvent: LiveData<Boolean>
        get() = _newNextOfKinEvent

    private var isInitNextOfKin = false

    fun start() {
        loadNextOfKin {}
        isInitNextOfKin = true
    }

    fun onResume() {
        if (isInitNextOfKin) {
            start()
        }
    }

    fun addNewNextOfKin() {
        _newNextOfKinEvent.value = true
    }

    fun loadNextOfKin(callback: ((List<NextOfKin>) -> Unit)?) {
        nextOfKinRepository.getNextOfKin(object : NextOfKinDataSource.LoadNextOfKinCallback {
            override fun onNextOfKinLoaded(nextOfKin: List<NextOfKin>) {
                _items.value = nextOfKin
                callback?.let {
                    it(nextOfKin)
                }
            }

            override fun onDataNotAvailable() {
                _items.value = emptyList()
                callback?.let {
                    it(emptyList())
                }
                _emptyListEvent.value = true
            }
        })
    }

    fun deleteNextOfKinInCache() {
        for (phoneNumber in deleteCache) {
            deleteNextOfKin(phoneNumber)
        }
        deleteCache.clear()
    }
    
    private fun deleteNextOfKin(phoneNumber: String) {
        nextOfKinRepository.deleteNextOfKin(phoneNumber)
        loadNextOfKin {}
    }
}