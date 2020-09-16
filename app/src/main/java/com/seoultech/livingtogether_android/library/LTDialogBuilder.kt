package com.seoultech.livingtogether_android.library

import android.view.View
import androidx.annotation.LayoutRes

class LTDialogBuilder {

    private var title: String? = null

    private var horizontalButtons: List<LTDialogButton>? = null

    private var verticalButtons: List<LTDialogButton>? = null

    @LayoutRes private var contentRes: Int? = null

    fun build(): LTDialog {
        return LTDialog(title, horizontalButtons, verticalButtons)
    }

    fun setTitle(title: String): LTDialogBuilder {
        this.title = title
        return this
    }

    fun addHorizontalButton(text: String, listener: ((dialog: LTDialog, contentView: View?) -> Unit)?): LTDialogBuilder {
        if (horizontalButtons == null) {
            horizontalButtons = mutableListOf()
        }
        (horizontalButtons as MutableList<LTDialogButton>).add(LTDialogButton(text, listener))
        return this
    }

    fun addHorizontalButton(text: String): LTDialogBuilder {
        return addHorizontalButton(text, null)
    }

    fun addVerticalButton(text: String, listener: ((dialog: LTDialog, contentView: View?) -> Unit)?): LTDialogBuilder {
        if (verticalButtons == null) {
            verticalButtons = mutableListOf()
        }
        (verticalButtons as MutableList<LTDialogButton>).add(LTDialogButton(text, listener))
        return this
    }

    fun addVerticalButton(text: String): LTDialogBuilder {
        return addVerticalButton(text, null)
    }

    fun setContentView(@LayoutRes layoutRes: Int): LTDialogBuilder {
        this.contentRes = layoutRes
        return this
    }
}