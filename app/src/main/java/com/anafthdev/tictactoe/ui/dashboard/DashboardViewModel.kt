package com.anafthdev.tictactoe.ui.dashboard

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anafthdev.tictactoe.data.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(): ViewModel() {
	
	var selectedGameMode by mutableStateOf(GameMode.Computer)
	private set
	
	var bluetoothAvailable by mutableStateOf(false)
	private set
	
	fun checkBluetooth(context: Context) {
		bluetoothAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
	}
	
	fun updateGameMode(mode: GameMode) {
		selectedGameMode = mode
	}
	
}