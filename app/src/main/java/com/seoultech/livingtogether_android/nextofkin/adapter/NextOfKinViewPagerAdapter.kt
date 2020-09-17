package com.seoultech.livingtogether_android.nextofkin.adapter

import android.view.ViewGroup
import com.seoultech.livingtogether_android.base.BaseViewPagerAdapter
import com.seoultech.livingtogether_android.base.BaseViewPagerHolder
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.nextofkin.viewholder.NextOfKinViewPagerHolder

class NextOfKinViewPagerAdapter(maxItem: Int) : BaseViewPagerAdapter<NextOfKin>(maxItem) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewPagerHolder<NextOfKin, *> {
        return NextOfKinViewPagerHolder(parent)
    }
}