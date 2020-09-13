package com.seoultech.livingtogether_android.nextofkin.viewholder

import android.view.ViewGroup
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewHolder
import com.seoultech.livingtogether_android.databinding.ItemNextOfKinBinding
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin

class NOKViewHolder(parent: ViewGroup) : BaseViewHolder<NextOfKin, ItemNextOfKinBinding>(R.layout.item_next_of_kin, parent) {
    override fun bind(data: NextOfKin) {
        binding.nextOfKin = data
    }
}