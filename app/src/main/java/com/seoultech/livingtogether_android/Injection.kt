package com.seoultech.livingtogether_android

import android.content.Context
import com.seoultech.livingtogether_android.database.LivingTogetherDatabase
import com.seoultech.livingtogether_android.nok.data.source.NextOfKinLocalDataSource
import com.seoultech.livingtogether_android.nok.data.source.NextOfKinRepository
import com.seoultech.livingtogether_android.util.AppExecutors

object Injection {

    fun provideNextOfKinRepository(context: Context): NextOfKinRepository {
        val database = LivingTogetherDatabase.getInstance(context)
        return NextOfKinRepository.getInstance(
            NextOfKinLocalDataSource.getInstance(AppExecutors(), database.nokDao()))
    }
}