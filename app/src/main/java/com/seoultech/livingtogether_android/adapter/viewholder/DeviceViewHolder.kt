package com.seoultech.livingtogether_android.adapter.viewholder

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.DeviceItemMainBinding
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import java.util.*

class DeviceViewHolder(parent: ViewGroup) :
    BaseViewHolder<DeviceEntity, DeviceItemMainBinding>(R.layout.device_item_main, parent) {

    override fun bind(data: DeviceEntity) {

        binding.run {
            textNameSensorItem.text = data.deviceType
            textLocationSensorItem.text = data.location
            val calendar = GregorianCalendar()

            //Todo: 임시로 현재 시간과 등록 시간의 차로 몇 분 전임을 표현함. 수정 필요
            val timeGap = calendar.timeInMillis - data.lastDetectionOfActionSignal!!

            textLastDetectionSensorItem.text = "${timeGap / 10000} 분 전"
            imageSenorItem.setImageResource(
                when (data.deviceType) {
                    "발판" -> R.drawable.ic_foothole_switch
                    else -> R.drawable.ic_foothole_switch
                }
            )
            /*viewIsWorkingSensorItem.setBackgroundColor(
                when (data.isAvailable) {
                    true -> R.color.stateGrean
                    else -> R.color.gray
                }
            )*/
            //FIXME : View 의 색이 제대로 먹지 않음. 상태에 따라 정확한 색을 표현하도록 고쳐야함

        }
    }
}