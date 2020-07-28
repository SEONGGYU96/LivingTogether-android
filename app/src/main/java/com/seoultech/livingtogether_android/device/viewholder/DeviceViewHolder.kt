package com.seoultech.livingtogether_android.device.viewholder

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.DeviceItemMainBinding
import com.seoultech.livingtogether_android.device.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.device.model.DeviceEntity

class DeviceViewHolder(parent: ViewGroup, val listener: DeviceAdapter.OnDeviceClickListener?) :
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
            
            viewStateCircleItem.backgroundTintList = when (data.isAvailable) {
                true -> ColorStateList.valueOf(ContextCompat.getColor(this.root.context, R.color.stateGrean))
                else -> ColorStateList.valueOf(ContextCompat.getColor(this.root.context, R.color.gray))
            }

            cardviewItemdeviceRoot.setOnClickListener {
                listener?.onClick(data)
            }
        }
    }
}