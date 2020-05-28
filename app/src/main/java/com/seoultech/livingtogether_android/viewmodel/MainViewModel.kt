package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.model.NOKData
import com.seoultech.livingtogether_android.model.SensorData
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DataBaseManager.getInstance(application)

    var sensors = ObservableField<List<SensorData>>(mutableListOf())

    var noks : LiveData<List<NOKEntity>>

    init {
        var data = mutableListOf<SensorData>()

        val data1 = SensorData("발판 센서", "화장실", 3, true, "발판")
        val data2 = SensorData("발판 센서", "안방", 2, true, "발판")
        val data3 = SensorData("발판 센서", "현관", 5, true, "발판")
        val data4 = SensorData("발판 센서", "화장실", 12, false, "발판")

        data.add(data1)
        data.add(data2)
        data.add(data3)
        data.add(data4)

        sensors.set(data)

        noks = getNOKAll()
    }

    fun getNOKAll(): LiveData<List<NOKEntity>> {
        return db.nokDao().getAllObservable()
    }
}