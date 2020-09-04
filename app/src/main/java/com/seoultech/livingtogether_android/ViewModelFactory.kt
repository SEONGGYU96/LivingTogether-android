package com.seoultech.livingtogether_android

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.nok.data.source.NextOfKinRepository
import com.seoultech.livingtogether_android.nok.viewmodel.NextOfKinViewModel

class ViewModelFactory private constructor(
    private val nextOfKinRepository: NextOfKinRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(NextOfKinViewModel::class.java) ->
                    NextOfKinViewModel(nextOfKinRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideNextOfKinRepository(application.applicationContext))
                    .also { INSTANCE = it }
            }
    }
}