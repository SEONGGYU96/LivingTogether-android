package com.seoultech.livingtogether_android.debug.scan.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.debug.scan.adapter.viewholder.BeaconSignalDetectedTimeViewHolder
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity

class ScanServiceTestAdapter() : BaseAdapter<SignalHistoryEntity>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SignalHistoryEntity, *> {
        return BeaconSignalDetectedTimeViewHolder(parent)
    }
}
