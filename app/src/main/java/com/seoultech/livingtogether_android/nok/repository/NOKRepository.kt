package com.seoultech.livingtogether_android.nok.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.base.BaseRepository
import com.seoultech.livingtogether_android.nok.dao.NOKDao
import com.seoultech.livingtogether_android.nok.model.NOKEntity
import java.lang.Exception

class NOKRepository : BaseRepository<NOKEntity>() {

    private val dao: NOKDao by lazy { ApplicationImpl.db.nokDao() }

    private val observableNOK: LiveData<List<NOKEntity>> by lazy { dao.getAllObservable() }

    fun getAllObservable() : LiveData<List<NOKEntity>> {
        Log.d(TAG, "getAllObservable : ${observableNOK.value}")
        return observableNOK
    }

    fun getAll() : List<NOKEntity> {
        val value = dao.getAll()
        Log.d(TAG, "getAll : $value")
        return value
    }

    fun getNOK(phone: String) : NOKEntity {
        val value = dao.getNOK(phone)
        Log.d(TAG, "getAll : $value")
        return value
    }

    public override fun insert(entity: NOKEntity) {
        super.insert(entity)
        try {
            dao.insert(entity)
        } catch (e: Exception) {
            Toast.makeText(ApplicationImpl.getInstance(), "이미 보호자로 등록되어 있습니다.", Toast.LENGTH_SHORT).show()
        }
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