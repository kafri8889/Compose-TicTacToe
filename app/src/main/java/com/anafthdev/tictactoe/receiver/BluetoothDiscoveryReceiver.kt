package com.anafthdev.tictactoe.receiver

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.anafthdev.tictactoe.common.BluetoothController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BluetoothDiscoveryReceiver : BroadcastReceiver() {
	
	@Inject lateinit var bluetoothController: BluetoothController
	
	@SuppressLint("MissingPermission")
	override fun onReceive(context: Context?, intent: Intent) {
		
		when (intent.action) {
			BluetoothDevice.ACTION_FOUND -> {
				val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
				
				Timber.i("Device found: ${device?.name}")
				
				if (device != null) {
					CoroutineScope(Dispatchers.Main).launch {
						bluetoothController.addDevice(device)
					}
				}
			}
		}
	}
}