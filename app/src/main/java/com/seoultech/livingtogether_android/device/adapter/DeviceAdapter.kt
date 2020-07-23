package com.seoultech.livingtogether_android.device.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.device.model.DeviceEntity
import com.seoultech.livingtogether_android.device.viewholder.DeviceViewHolder

class DeviceAdapter : BaseAdapter<DeviceEntity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(
            parent
        )
    }
}