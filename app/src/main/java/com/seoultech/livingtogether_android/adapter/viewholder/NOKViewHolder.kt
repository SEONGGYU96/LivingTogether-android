package com.seoultech.livingtogether_android.adapter.viewholder

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.model.NOKData
import com.seoultech.livingtogether_android.databinding.NokItemMainBinding

class NOKViewHolder(parent: ViewGroup) : BaseViewHolder<NOKData, NokItemMainBinding>(R.layout.nok_item_main, parent) {
    override fun bind(data: NOKData) {
        binding.run {
            textNameNokItem.text = data.name
            textPhoneNokItem.text = data.phone
        }
    }
}