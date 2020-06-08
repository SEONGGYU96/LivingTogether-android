package com.seoultech.livingtogether_android.debug.scan.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.debug.scan.adapter.viewholder.BeaconSignalDetectedTimeViewHolder
import com.seoultech.livingtogether_android.debug.model.SignalTestModel

class ScanServiceTestAdapter() : BaseAdapter<SignalTestModel>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SignalTestModel, *> {
        return BeaconSignalDetectedTimeViewHolder(parent)
    }
}
