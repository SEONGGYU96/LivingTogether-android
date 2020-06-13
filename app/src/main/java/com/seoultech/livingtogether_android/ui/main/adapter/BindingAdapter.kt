package com.seoultech.livingtogether_android.ui.main.adapter

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visible")
    fun setVisible(view: View, data: List<Nothing>?) {
        data?.let {
            var onOff = data.isNotEmpty()

            if (view is ConstraintLayout) {
                onOff = !onOff
            }

            if (onOff) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }
}