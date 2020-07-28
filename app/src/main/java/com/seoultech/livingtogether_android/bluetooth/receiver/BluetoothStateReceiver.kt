package com.seoultech.livingtogether_android.bluetooth.receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.seoultech.livingtogether_android.bluetooth.model.BluetoothLiveData
import com.seoultech.livingtogether_android.bluetooth.service.ScanService

class BluetoothStateReceiver : BroadcastReceiver() {
    companion object {
        private val TAG = BluetoothStateReceiver::class.java.simpleName
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)

            if (state == BluetoothAdapter.STATE_OFF) {
                Log.d(TAG, "onReceive() : BluetoothAdapter.STATE_OFF")

                sendBtState(context, false)

                BluetoothLiveData.value = false

            } else if (state == BluetoothAdapter.STATE_ON) {
                Log.d(TAG, "onReceive() : BluetoothAdapter.STATE_ON")

                sendBtState(context, true)

                BluetoothLiveData.value = true
            }
        }
    }

    private fun sendBtState(context: Context, state: Boolean) {
        val intent = Intent(context, ScanService::class.java)

        when (state) {
            true -> intent.putExtra(ScanService.FLAG_BT_CHANGED, ScanService.FLAG_BT_CHANGED_VALUE_ON)
            false -> intent.putExtra(ScanService.FLAG_BT_CHANGED, ScanService.FLAG_BT_CHANGED_VALUE_OFF)
        }
        context.startService(intent)
    }
}