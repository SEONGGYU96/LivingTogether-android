package com.seoultech.livingtogether_android.viewmodel

import androidx.databinding.ObservableField
import com.seoultech.livingtogether_android.data.NOKData
import com.seoultech.livingtogether_android.data.SensorData

class MainViewModel {
    var sensors = ObservableField<List<SensorData>>(mutableListOf())

    var noks = ObservableField<List<NOKData>>(mutableListOf())

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

        var nok = mutableListOf<NOKData>()

        val nok1 = NOKData("김성규","010-2222-3444")
        val nok2 = NOKData("이영준","010-2235-3734")
        val nok3 = NOKData("강석민","010-2277-3344")
        val nok4 = NOKData("위정빈","010-2232-1254")

        nok.add(nok1)
        nok.add(nok2)
        nok.add(nok3)
        nok.add(nok4)

        noks.set(nok)
    }
}