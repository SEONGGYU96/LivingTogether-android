package com.seoultech.livingtogether_android.util

fun Int.toDP(): Int {
    return MetricsUtil.convertPixelsToDp(this.toFloat(), null).toInt()
}

fun Int.toPixel() : Float {
    return MetricsUtil.convertDpToPixel(this.toFloat(), null)
}