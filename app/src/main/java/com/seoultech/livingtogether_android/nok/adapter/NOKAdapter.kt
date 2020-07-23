package com.seoultech.livingtogether_android.nok.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.nok.model.NOKEntity
import com.seoultech.livingtogether_android.nok.viewholder.NOKViewHolder

class NOKAdapter : BaseAdapter<NOKEntity>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NOKEntity, *> {
        return NOKViewHolder(
            parent
        )
    }
}