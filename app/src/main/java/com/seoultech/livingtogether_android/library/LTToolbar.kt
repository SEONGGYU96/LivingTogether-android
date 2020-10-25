package com.seoultech.livingtogether_android.library

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.ui.profile.EditProfileActivity
import com.seoultech.livingtogether_android.ui.profile.ProfileActivity
import kotlinx.android.synthetic.main.view_livingtogether_toolbar.view.*

class LTToolbar : ConstraintLayout {

    companion object {
        private const val layoutRes = R.layout.view_livingtogether_toolbar
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(layoutRes, this, true)
        this.clipChildren = false
    }

    fun setBackButton(listener: OnClickListener?) {
        this.imagebutton_toolbar_back.run {
            visibility = View.VISIBLE
            setOnClickListener {
                if (listener != null) {
                    listener.onClick(this)
                } else {
                    (context as Activity).finish()
                }
            }
        }
    }

    fun setBackButton() {
        setBackButton(null)
    }

    fun setMyPageButton() {
        button_toolbar_righttext.visibility = View.GONE
        this.imagebutton_toolbar_mypage.run {
            visibility = View.VISIBLE
            setOnClickListener {
                context.startActivity(Intent(context as Activity, ProfileActivity::class.java))
            }
        }
    }

    fun clearButton() {
        this.imagebutton_toolbar_back.run {
            visibility = View.GONE
            setOnClickListener(null)
        }

        this.imagebutton_toolbar_mypage.run {
            visibility = View.GONE
            setOnClickListener(null)
        }
    }

    fun setRightTextButton(text: String, listener: OnClickListener) {
        imagebutton_toolbar_mypage.visibility = View.GONE

        this.button_toolbar_righttext.run {
            visibility = View.VISIBLE
            setText(text)
            setOnClickListener {
                listener.onClick(this)
            }
        }
    }
}