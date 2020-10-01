package com.seoultech.livingtogether_android.nextofkin.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseViewPagerHolder
import com.seoultech.livingtogether_android.databinding.ItemViewpagerBinding
import com.seoultech.livingtogether_android.nextofkin.adapter.NextOfKinListAdapter
import com.seoultech.livingtogether_android.nextofkin.data.NextOfKin
import com.seoultech.livingtogether_android.util.MarginDecoration

class NextOfKinViewPagerHolder(parent: ViewGroup) : BaseViewPagerHolder<NextOfKin, ItemViewpagerBinding>(R.layout.item_viewpager, parent) {
    override fun bind(data: List<NextOfKin>) {
        binding.recyclerviewViewpager2item.run {
            adapter = NextOfKinListAdapter().apply {
                setList(data)
            }
            addItemDecoration(MarginDecoration(itemView.context, 25, RecyclerView.VERTICAL))
        }
    }
}