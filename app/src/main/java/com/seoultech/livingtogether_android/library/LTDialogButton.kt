package com.seoultech.livingtogether_android.library

import androidx.databinding.ViewDataBinding

data class LTDialogButton<B: ViewDataBinding?>(
    val text: String,
    val listener: ((dialog: LTDialog<B>, viewBinding: B?) -> Unit)?
)