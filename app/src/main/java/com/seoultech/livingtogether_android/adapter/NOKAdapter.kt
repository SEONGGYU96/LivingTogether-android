package com.seoultech.livingtogether_android.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.adapter.viewholder.NOKViewHolder
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.model.NOKData

class NOKAdapter : BaseAdapter<NOKData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NOKData, *> {
        return NOKViewHolder(parent)
    }
}