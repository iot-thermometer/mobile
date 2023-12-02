package com.pawlowski.temperaturemanager

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothStateBroadcastReceiver(private val onBluetoothStateChanged: (Boolean) -> Unit) :
    BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.STATE_OFF -> onBluetoothStateChanged(false)
                BluetoothAdapter.STATE_ON -> onBluetoothStateChanged(true)
            }
        }
    }
}
