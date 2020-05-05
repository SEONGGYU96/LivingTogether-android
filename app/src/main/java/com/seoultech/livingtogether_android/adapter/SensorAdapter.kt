package com.seoultech.livingtogether_android.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.adapter.viewholder.SensorViewHolder
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.data.SensorData

class SensorAdapter : BaseAdapter<SensorData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorViewHolder {
        return SensorViewHolder(parent)
    }
}