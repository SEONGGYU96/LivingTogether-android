package com.seoultech.livingtogether_android.library

import android.view.View

data class LTDialogButton(
    val text: String,
    val listener: ((dialog: LTDialog, contentView: View?) -> Unit)?
)