package com.seoultech.livingtogether_android.device.viewholder

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.ViewGroup
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.ItemSensorBinding
import com.seoultech.livingtogether_android.device.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.device.data.Device

class DeviceViewHolder(parent: ViewGroup, val listener: DeviceAdapter.OnDeviceClickListener?) :
    BaseViewHolder<Device, ItemSensorBinding>(R.layout.item_sensor, parent) {

    override fun bind(data: Device) {
        val application = ApplicationImpl.getInstance()

        binding.run {
            device = data

            imageviewItemdeviceState.run {
                background = ShapeDrawable(OvalShape())
                clipToOutline = true
            }

            textviewItemdeviceLastdetactedtime.text = data.getLastDetectedTimeToMinuet()

            textviewItemdeviceName.text = when (data.deviceType) {
                "발판" -> "발판 센서"
                else -> "알 수 없는 기기"
            }

            if (!data.updateIsAvailable()) {
                textviewItemdeviceLastdetactedtime.run {
                    text = application.getString(R.string.sensor_no_response)
                    setTextColor(application.getColor(R.color.colorPlainText))
                }
                constraintlayoutItemdeviceRoot.setBackgroundColor(application.getColor(R.color.colorRegisterButtonGray))
            }

            constraintlayoutItemdeviceRoot.setOnClickListener {
                listener?.onClick(data)
            }
        }
    }
}