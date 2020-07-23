package com.seoultech.livingtogether_android.ui.main.adapter

import android.content.Intent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.seoultech.livingtogether_android.ui.nok.AddNOKActivity
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.util.Constant

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
                view.visibility = View.INVISIBLE
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:onClick")
    fun onClick(view: View, target: Int) {
        view.setOnClickListener {
            when (target) {
                Constant.SCAN_ACTIVITY -> view.context.startActivity(
                    Intent(view.context, ScanActivity::class.java
                    )
                )

                Constant.ADD_NOK_ACTIVITY -> view.context.startActivity(
                    Intent(view.context, AddNOKActivity::class.java
                    )
                )
            }
        }
    }
}