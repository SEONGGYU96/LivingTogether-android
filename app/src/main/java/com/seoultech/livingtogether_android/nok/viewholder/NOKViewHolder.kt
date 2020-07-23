package com.seoultech.livingtogether_android.nok.viewholder

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.NokItemMainBinding
import com.seoultech.livingtogether_android.nok.model.NOKEntity

class NOKViewHolder(parent: ViewGroup) : BaseViewHolder<NOKEntity, NokItemMainBinding>(R.layout.nok_item_main, parent) {
    override fun bind(data: NOKEntity) {
        binding.run {
            textNameNokItem.text = data.name
            textPhoneNokItem.text = data.phoneNum
        }
    }
}