package com.seoultech.livingtogether_android.nextofkin.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.nextofkin.viewholder.NextOfKinViewHolder

class NextOfKinListAdapter : BaseAdapter<NextOfKin>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NextOfKin, *> {
        return NextOfKinViewHolder(parent, isList = true, isEdit = false) {}
    }
}