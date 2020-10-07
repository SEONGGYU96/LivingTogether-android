package com.seoultech.livingtogether_android.nextofkin.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseAdapter
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.nextofkin.viewholder.NextOfKinViewHolder

class NextOfKinEditAdapter(private val deleteListener: (String, Boolean) -> Unit) : BaseAdapter<NextOfKin>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<NextOfKin, *> {
        return NextOfKinViewHolder(parent, isList = true, isEdit = true) {
            deleteListener(getItem(it).phoneNumber, itemCount == 1)
            if (itemCount != 1) {
                remove(it)
            }
        }
    }
}