package com.seoultech.livingtogether_android.nok.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.nok.data.NextOfKin
import com.seoultech.livingtogether_android.nok.viewholder.NOKViewHolder

class NOKAdapter : BaseAdapter<NextOfKin>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NextOfKin, *> {
        return NOKViewHolder(
            parent
        )
    }
}