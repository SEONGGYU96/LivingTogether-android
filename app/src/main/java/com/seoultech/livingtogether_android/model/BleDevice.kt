package com.seoultech.livingtogether_android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 비콘 기기 모델
 */
@Parcelize
data class BleDevice @ExperimentalUnsignedTypes constructor(
    /** UUID string  */
    val uuid: String,
    val rssi: Int,
    val major: UInt,
    /** minor value  */
    val minor: UInt,
    val tx: Int,
    /** 비콘 mac address  */
    val address: String
) : Parcelable