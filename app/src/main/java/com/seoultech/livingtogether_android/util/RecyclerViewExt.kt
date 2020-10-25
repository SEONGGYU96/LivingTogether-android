package com.seoultech.livingtogether_android.util

import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
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

@BindingAdapter("setGrayTint")
fun ImageView.setGrayTint(isAvailable: Boolean) {
    if (isAvailable) {
        this.imageTintList = null
    } else {
        this.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context.applicationContext, R.color.colorRegisterButtonGray))
    }
}