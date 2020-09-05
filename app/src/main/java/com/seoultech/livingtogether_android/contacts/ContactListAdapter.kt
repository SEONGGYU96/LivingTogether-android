package com.seoultech.livingtogether_android.contacts

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin

class ContactListAdapter : BaseAdapter<NextOfKin>() {
    private var listener: OnContactClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NextOfKin, *> {
        return ContactViewHolder(parent, listener)
    }

    interface OnContactClickListener {
        fun onClick()
    }

    fun setOnContactClickListener(listener: OnContactClickListener) {
        this.listener = listener
    }
}