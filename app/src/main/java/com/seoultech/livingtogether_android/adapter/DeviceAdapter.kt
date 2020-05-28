package com.seoultech.livingtogether_android.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.adapter.viewholder.DeviceViewHolder
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.model.SensorData
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

class DeviceAdapter : BaseAdapter<DeviceEntity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        return DeviceViewHolder(parent)
    }
}