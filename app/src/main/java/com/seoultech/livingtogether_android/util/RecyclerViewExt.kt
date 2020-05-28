package com.seoultech.livingtogether_android.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.base.BaseAdapter

@BindingAdapter("replaceAll")
fun RecyclerView.replaceAll(list: List<Nothing>?) {
    if (adapter != null) {
        (adapter as BaseAdapter<*>).run {
            if (list != null) {
                setList(list)
            }
        }
    }
}