package com.seoultech.livingtogether_android.adapter.viewholder

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.data.SensorData
import com.seoultech.livingtogether_android.databinding.SensorItemMainBinding

class SensorViewHolder(parent: ViewGroup) :
    BaseViewHolder<SensorData, SensorItemMainBinding>(R.layout.sensor_item_main, parent) {
    override fun bind(data: SensorData) {
        binding.run {
            textNameSensorItem.text = data.name
            textLocationSensorItem.text = data.loction
            textLastDetectionSensorItem.text = data.lastDetectionEvent.toString() + "시간 전"
            imageSenorItem.setImageResource(
                when (data.kind) {
                    "발판" -> R.drawable.ic_foothole_switch
                    else -> R.drawable.ic_foothole_switch
                }
            )
            /*viewIsWorkingSensorItem.setBackgroundColor(
                when (data.isWorking) {
                    true -> R.color.stateGrean
                    else -> R.color.gray
                }
            )*/
            //FIXME : View 의 색이 제대로 먹지 않음. 상태에 따라 정확한 색을 표현하도록 고쳐야함

        }
    }
}