package com.seoultech.livingtogether_android.contacts

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.nok.model.NOKEntity

class ContactListAdapter : BaseAdapter<NOKEntity>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NOKEntity, *> {
        return ContactViewHolder(parent)
    }
}