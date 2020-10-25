package com.seoultech.livingtogether_android.device.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.viewholder.DeviceViewHolder

class DeviceAdapter : BaseAdapter<Device>() {
    var listener: OnDeviceClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(parent, listener)
    }

    fun setOnDeviceClickListener(listener: OnDeviceClickListener) {
        this.listener = listener
    }

    interface OnDeviceClickListener {
        fun onClick(data: Device)
    }
}