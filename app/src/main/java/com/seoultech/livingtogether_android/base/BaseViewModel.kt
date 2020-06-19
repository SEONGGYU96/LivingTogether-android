package com.seoultech.livingtogether_android.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.model.room.DataBaseManager

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val db : DataBaseManager by lazy { ApplicationImpl.db }
    protected val TAG = javaClass.simpleName
}