package com.seoultech.livingtogether_android.library

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

class LTDialogBuilder<B: ViewDataBinding?, T: Any?> {

    private var title: String? = null

    @LayoutRes private var contentRes: Int? = null

    private var horizontalButtons: List<LTDialogButton<B>>? = null

    private var verticalButtons: List<LTDialogButton<B>>? = null

    private var injection: ((B) -> Unit)? = null

    fun build(): LTDialog<B> {
        return LTDialog(title, contentRes, horizontalButtons, verticalButtons, injection)
    }

    fun setTitle(title: String): LTDialogBuilder<B, T> {
        this.title = title
        return this
    }

    fun addHorizontalButton(text: String, listener: ((dialog: LTDialog<B>, viewBinding: B?) -> Unit)?): LTDialogBuilder<B, T> {
        if (horizontalButtons == null) {
            horizontalButtons = mutableListOf()
        }
        (horizontalButtons as MutableList<LTDialogButton<B>>).add(LTDialogButton(text, listener))
        return this
    }

    fun addHorizontalButton(text: String): LTDialogBuilder<B, T> {
        return addHorizontalButton(text, null)
    }

    fun addVerticalButton(text: String, listener: ((dialog: LTDialog<B>, viewBinding: B?) -> Unit)?): LTDialogBuilder<B, T> {
        if (verticalButtons == null) {
            verticalButtons = mutableListOf()
        }
        (verticalButtons as MutableList<LTDialogButton<B>>).add(LTDialogButton(text, listener))
        return this
    }

    fun addVerticalButton(text: String): LTDialogBuilder<B, T> {
        return addVerticalButton(text, null)
    }

    fun setContentView(@LayoutRes layoutRes: Int): LTDialogBuilder<B, T> {
        this.contentRes = layoutRes
        return this
    }

    fun injection(injection: (B) -> Unit): LTDialogBuilder<B, T> {
        this.injection = injection
        return this
    }
}