package com.seoultech.livingtogether_android.model.room

import androidx.room.TypeConverter
import com.seoultech.livingtogether_android.service.Signal

class Converters {
    @TypeConverter
    fun signalToString(signal: Signal?) : String? {
        return signal?.toString()
    }

    @TypeConverter
    fun fromSignal(signal: String?) : Signal? {
        return signal?.let{ Signal.valueOf(it)}
    }
}