package com.seoultech.livingtogether_android.nok.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.base.BaseRepository
import com.seoultech.livingtogether_android.nok.dao.NOKDao
import com.seoultech.livingtogether_android.nok.model.NOKEntity

class NOKRepository : BaseRepository<NOKEntity>() {

    private val dao: NOKDao by lazy { ApplicationImpl.db.nokDao() }

    private val observableNOK: LiveData<List<NOKEntity>> by lazy { dao.getAllObservable() }

    fun getAllObservable() : LiveData<List<NOKEntity>> {
        Log.d(TAG, "getAllObservable : ${observableNOK.value}")
        return observableNOK
    }

    public override fun insert(entity: NOKEntity) {
        super.insert(entity)
        return dao.insert(entity)
    }

    public override fun delete(entity: NOKEntity) {
        super.delete(entity)
        return dao.delete(entity)
    }

    public override fun update(entity: NOKEntity) {
        super.update(entity)
        return dao.update(entity)
    }
}