package com.seoultech.livingtogether_android.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewPagerHolder<T, B: ViewDataBinding>(@LayoutRes layoutRes: Int, parent: ViewGroup)
    : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)) {

    protected var binding: B = DataBindingUtil.bind(itemView)!!

    abstract fun bind(data: List<T>)
}