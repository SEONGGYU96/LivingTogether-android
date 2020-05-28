package com.seoultech.livingtogether_android.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.adapter.viewholder.NOKViewHolder
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity

class NOKAdapter : BaseAdapter<NOKEntity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NOKEntity, *> {
        return NOKViewHolder(parent)
    }
}