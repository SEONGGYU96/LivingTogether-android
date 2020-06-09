package com.seoultech.livingtogether_android.debug.scan.adapter.viewholder

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.ItemBeaconSignalDetectedTimeBinding
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity

class BeaconSignalDetectedTimeViewHolder(parent: ViewGroup) : BaseViewHolder<SignalHistoryEntity, ItemBeaconSignalDetectedTimeBinding>(
    R.layout.item_beacon_signal_detected_time, parent) {

    override fun bind(data: SignalHistoryEntity) {
        binding.run {
            textMajorScanTestItem.text = data.deviceMajor
            textDetectedTimeScanTestItem.text = data.getDetectedTimeToString()
        }
    }
}