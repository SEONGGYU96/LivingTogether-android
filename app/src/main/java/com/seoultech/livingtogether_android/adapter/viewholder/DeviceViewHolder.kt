package com.seoultech.livingtogether_android.adapter.viewholder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
            
            viewIsWorkingSensorItem.backgroundTintList = when (data.isAvailable) {
                true -> ColorStateList.valueOf(ContextCompat.getColor(this.root.context, R.color.stateGrean))
                else -> ColorStateList.valueOf(ContextCompat.getColor(this.root.context, R.color.gray))
            }
        }
    }
}