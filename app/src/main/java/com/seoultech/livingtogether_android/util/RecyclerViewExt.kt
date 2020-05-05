package com.seoultech.livingtogether_android.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.base.BaseAdapter

@BindingAdapter("replaceAll")
fun RecyclerView.replaceAll(list: List<Nothing>) {
    (adapter as BaseAdapter<*>).run {
        setList(list)
    }
}