package com.anafthdev.tictactoe.ui.pvp_bluetooth

import android.bluetooth.BluetoothDevice
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.tictactoe.common.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PvPBluetoothViewModel @Inject constructor(
	private val bluetoothController: BluetoothController
): ViewModel() {
	
	var isRefreshing by mutableStateOf(false)
	private set
	
	val availableBluetoothDevice = mutableStateListOf<BluetoothDevice>()
	
	init {
		viewModelScope.launch {
			bluetoothController.availableBluetoothDevice.collect { devices ->
				availableBluetoothDevice.apply {
					clear()
					addAll(devices)
				}
			}
		}
		
		viewModelScope.launch {
			bluetoothController.isDiscovering.collect { isDiscovering ->
				isRefreshing = isDiscovering
			}
		}
	}
	
	fun isBluetoothEnabled(): Boolean {
		return bluetoothController.isBluetoothEnabled()
	}
	
	fun isPaired(device: BluetoothDevice): Boolean {
		return bluetoothController.isPaired(device)
	}
	
	fun connect(device: BluetoothDevice) {
		bluetoothController.connect(device)
	}
	
	fun tryWrite() {
		bluetoothController.tryWrite()
	}
	
	fun tryRead() {
		bluetoothController.tryRead()
	}
	
	fun startDiscovery() {
		bluetoothController.startDiscovery()
	}
	
}