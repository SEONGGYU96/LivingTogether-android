package com.seoultech.livingtogether_android.util

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.base.BaseAdapter

@BindingAdapter("replaceAll")
fun RecyclerView.replaceAll(list: List<Nothing>?) {
    if (adapter != null) {
        (adapter as BaseAdapter<*>).run {
            if (list != null) {
                setList(list)
            } else {
                clear()
            }
        }
    }
}

@BindingAdapter("visibilityAsData")
fun TextView.visibilityAsData(list: List<Nothing>?) {
    visibility = if (list != null) {
        if (list.isEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    } else {
        View.VISIBLE
    }
}