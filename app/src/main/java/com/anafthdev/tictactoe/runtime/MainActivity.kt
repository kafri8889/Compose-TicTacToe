package com.anafthdev.tictactoe.runtime

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.anafthdev.tictactoe.common.BluetoothChatService
import com.anafthdev.tictactoe.common.BluetoothController
import com.anafthdev.tictactoe.extension.toast
import com.anafthdev.tictactoe.receiver.BluetoothDiscoveryReceiver
import com.anafthdev.tictactoe.runtime.navigation.TicTacToeNavigation
import com.anafthdev.tictactoe.theme.TicTacToeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	
	@Inject lateinit var bluetoothController: BluetoothController
	
	private val bluetoothDiscoveryReceiver = BluetoothDiscoveryReceiver()
	
	private val requestPermissionLauncher = registerForActivityResult(
		ActivityResultContracts.RequestMultiplePermissions()
	) { result ->
		val allPermissionGranted = result.values.all { it }
		
		if (!allPermissionGranted) {
			"You have to allow location permission and bluetooth to play".toast(this)
			
			startActivity(
				Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
					data = Uri.fromParts("package", packageName, null)
				}
			)
		}
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		WindowCompat.setDecorFitsSystemWindows(window, false)
		
		setContent {
			TicTacToeTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					TicTacToeNavigation()
				}
			}
		}
		
		bluetoothController.setListener(object : BluetoothController.BluetoothListener {
			override fun onConnected() {
				"Connected".toast(this@MainActivity)
			}
			
			override fun onFailedToConnect() {
				"Failed to connect".toast(this@MainActivity)
			}
			
			override fun onFailedToWrite() {
				"Failed to write".toast(this@MainActivity)
			}
		})
		
		val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
		registerReceiver(bluetoothDiscoveryReceiver, intentFilter)
	}
	
	override fun onResume() {
		super.onResume()
		
		if (bluetoothController.bluetoothService.state == BluetoothChatService.STATE_NONE) {
			bluetoothController.bluetoothService.start()
		}
	}
	
	fun checkPermission(): Boolean {
		val permissions = arrayListOf(
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
		)
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			permissions.add(Manifest.permission.BLUETOOTH_SCAN)
			permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
		}
		
		val allPermissionsGranted = permissions.all { permission ->
			ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
		}
		
//		(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED).toast(this)
//		(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED).toast(this)
		
		return allPermissionsGranted
	}
	
	fun requestPermission() {
		val permissionsToRequest = arrayListOf(
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
		)
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN)
		}
		
		requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
	}
	
	override fun onDestroy() {
		bluetoothController.onDestroy()
		unregisterReceiver(bluetoothDiscoveryReceiver)
		
		super.onDestroy()
	}
}
