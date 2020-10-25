package com.seoultech.livingtogether_android.signal.data.source.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.seoultech.livingtogether_android.signal.data.Signal
import com.seoultech.livingtogether_android.signal.data.source.SignalDataSource

class SignalRemoteDataSource(private val signalDatabase: DatabaseReference) : SignalDataSource {

    override fun getSignals(callback: SignalDataSource.LoadSignalsCallback) {
        signalDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                callback.onDataNotAvailable()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    callback.onDataNotAvailable()
                    return
                }
                val result = mutableListOf<Signal>()
                for (_snapshot in snapshot.children) {
                    _snapshot.getValue<Signal>()?.let { result.add(it) }
                }
                callback.onSignalsLoaded(result)
            }
        })
    }

    override fun saveSignal(signal: Signal) {
        signalDatabase.child(signal.detectiveTime.toString()).setValue(signal)
    }

    override fun deleteAllSignals() {
        signalDatabase.removeValue()
    }
}