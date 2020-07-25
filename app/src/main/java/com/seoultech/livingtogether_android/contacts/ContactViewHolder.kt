package com.seoultech.livingtogether_android.contacts

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.ItemContactListBinding
import com.seoultech.livingtogether_android.nok.model.NOKEntity

class ContactViewHolder(parent: ViewGroup) : BaseViewHolder<NOKEntity, ItemContactListBinding>(R.layout.item_contact_list, parent) {
    override fun bind(data: NOKEntity) {
        binding.run {
            textviewNameContactitem.text = data.name
            textviewPhoneContactitem.text = data.phoneNum
        }
    }
}