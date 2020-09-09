package com.seoultech.livingtogether_android.nextofkin.viewholder

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.NokItemMainBinding
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin

class NOKViewHolder(parent: ViewGroup) : BaseViewHolder<NextOfKin, NokItemMainBinding>(R.layout.nok_item_main, parent) {
    override fun bind(data: NextOfKin) {
        binding.run {
            textNameNokItem.text = data.name
            textPhoneNokItem.text = data.phoneNumber
        }
    }
}