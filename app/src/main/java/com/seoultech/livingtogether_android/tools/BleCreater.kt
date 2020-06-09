package com.seoultech.livingtogether_android.tools

import android.bluetooth.BluetoothDevice
import com.seoultech.livingtogether_android.model.BleDevice
import java.nio.ByteBuffer
import java.util.*
import kotlin.experimental.and

class BleCreater {
    companion object {
        @ExperimentalUnsignedTypes
        fun create(device: BluetoothDevice, rssi: Int, scanRecord: ByteArray): BleDevice {

            val mostByte = ByteArray(8)
            val leastByte = ByteArray(8)
            System.arraycopy(scanRecord, 9, mostByte, 0, 8)
            System.arraycopy(scanRecord, 17, leastByte, 0, 8)

            val buffer = ByteBuffer.allocate(java.lang.Long.SIZE)
            buffer.put(mostByte)
            buffer.flip()
            val mostSigBits = buffer.long

            buffer.clear()
            buffer.put(leastByte)
            buffer.flip()
            val leastSigBits = buffer.long


            val uuid = UUID(mostSigBits, leastSigBits)
            val major: UInt =
                (scanRecord[25].toUByte() and 0xff.toUByte()).toUInt() shl 8 or (scanRecord[26].toUByte() and 0xff.toUByte()).toUInt()
            val minor: UInt =
                (scanRecord[27].toUByte() and 0xff.toUByte()).toUInt() shl 8 or (scanRecord[28].toUByte() and 0xff.toUByte()).toUInt()

            var tx = (scanRecord[29] and 0xff.toByte()).toInt()
            if (tx != 0) {
                tx -= 256
            }

            return BleDevice(
                uuid.toString().toLowerCase(Locale.getDefault()),
                rssi, major, minor, tx, device.address
            )
        }
    }
}