package com.seoultech.livingtogether_android.adapter.viewholder

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.DeviceItemMainBinding
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

class DeviceViewHolder(parent: ViewGroup) :
    BaseViewHolder<DeviceEntity, DeviceItemMainBinding>(R.layout.device_item_main, parent) {

    override fun bind(data: DeviceEntity) {

        binding.run {
            textNameSensorItem.text = data.deviceType
            textLocationSensorItem.text = data.location

            textLastDetectionSensorItem.text = data.getLastDetectedTimeToMinuet()

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