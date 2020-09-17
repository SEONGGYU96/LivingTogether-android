package com.seoultech.livingtogether_android.library

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.util.toPixel
import kotlinx.android.synthetic.main.item_dialog_button.view.*
import kotlinx.android.synthetic.main.view_livingtogether_dialog.view.*


class LTDialog(
    private val title: String?, private val horizontalButtons: List<LTDialogButton>?,
    private val verticalButtons: List<LTDialogButton>?) : DialogFragment() {

    companion object {
        private const val TAG = "LTDialog"
    }

    private var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_livingtogether_dialog, container)

        initView(view, inflater)

        if (dialog != null) {
            val window: Window? = dialog!!.window
            if (window != null) {
                //다이얼로그의 윤곽선을 둥글게 깎기
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.requestFeature(Window.FEATURE_NO_TITLE)
            } else {
                Log.e(TAG, "Window is null.")
            }
        } else {
            Log.e(TAG, "Dialog is null.")
        }

        return view
    }

    private fun initView(view: View, inflater: LayoutInflater) {
        val textViewDialogTitle = view.textview_dialog_title
        val linearLayoutDialogContent = view.linearlayout_dialog_content
        val linearLayoutDialogButtons = view.linearlayout_dialog_buttons

        initTextView(textViewDialogTitle, title)

        initButtons(linearLayoutDialogButtons, inflater)
    }

    private fun initButtons(linearLayoutDialogButtons: LinearLayout, inflater: LayoutInflater) {
        verticalButtons?.let {
            makeButtons(it, inflater, linearLayoutDialogButtons, LinearLayout.VERTICAL)
        }

        horizontalButtons?.let {
            //가로 버튼을 담을 하나의 LinearLayout을 생성
            val verticalButtonGroup = LinearLayout(context)
            verticalButtonGroup.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            //생성한 LinearLayout에 버튼들을 추가
            makeButtons(it, inflater, verticalButtonGroup, LinearLayout.HORIZONTAL)

            //버튼이 추가된 LinearLayout을 다이얼로그에 추가
            linearLayoutDialogButtons.addView(verticalButtonGroup)
        }
    }

    private fun makeButtons(buttons: List<LTDialogButton>, inflater: LayoutInflater,
                            container: ViewGroup, orientation: Int) {

        for (lTDialogButton in buttons) {

            //버튼을 inflate하고 아직 붙이지는 않음
            val button = inflater.inflate(R.layout.item_dialog_button, container, false)

            //가로 버튼이라면 weight 값을 줘 균등하게 가로 길이를 가지도록 함
            if (orientation == LinearLayout.HORIZONTAL) {
                button.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            //첫 번째 버튼이 아니라면
            if (lTDialogButton != buttons[0]) {
                //구분선 삽입
                setButtonDivider(container, orientation)
            }

            //버튼에 문자열 적용
            button.textview_dialog_buttonitem.text = lTDialogButton.text

            //버튼에 리스너 적용
            lTDialogButton.listener?.let {
                button.setOnClickListener {
                    (lTDialogButton.listener)(this, contentView)
                }
            }

            //버튼 추가
            container.addView(button)
        }
    }

    private fun setButtonDivider(container: ViewGroup, orientation: Int) {
        //뷰 하나 생성
        val divider = View(context)

        //간격을 1로 하여 선처럼 보이게 함
        divider.layoutParams = if (orientation == LinearLayout.HORIZONTAL) {
            LinearLayout.LayoutParams(1.toPixel(), LinearLayout.LayoutParams.MATCH_PARENT)
        } else {
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1.toPixel())
        }

        //색 지정
        divider.setBackgroundResource(R.color.colorDividerGray)

        //삽입
        container.addView(divider)
    }

    private fun initTextView(textView: TextView, text: String?) {
        if (text != null) {
            textView.text = text
        } else {
            textView.visibility = View.GONE
        }
    }
}