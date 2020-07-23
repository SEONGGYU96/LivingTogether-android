package com.seoultech.livingtogether_android.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val TAG = javaClass.simpleName

    val finishHandler = MutableLiveData<Boolean>()
}