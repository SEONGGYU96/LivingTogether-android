package com.seoultech.livingtogether_android.nok.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.nok.model.NOKEntity
import com.seoultech.livingtogether_android.nok.repository.NOKRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NOKViewModel(application: Application) : BaseViewModel(application) {

    private val nokRepository: NOKRepository by lazy { NOKRepository() }

    var noks = getAll()

    var newNok = NOKEntity()

    fun getAll(): LiveData<List<NOKEntity>> {
        return nokRepository.getAllObservable()
    }

    fun insert() {
        viewModelScope.launch(Dispatchers.IO) {
            nokRepository.insert(newNok)
        }
        finishHandler.value = true
    }
}