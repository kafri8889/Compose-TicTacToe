package com.anafthdev.tictactoe.uicomponent

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothDeviceItem(
	device: BluetoothDevice,
	modifier: Modifier = Modifier,
	onConnect: () -> Unit
) {

	Card(modifier = modifier) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.padding(8.dp)
		) {
			Column {
				Text(
					text = device.name ?: "-No name-",
					style = MaterialTheme.typography.titleMedium
				)
				
				Spacer(modifier = Modifier.height(8.dp))
				
				Text(
					text = device.address,
					style = MaterialTheme.typography.bodyMedium
				)
			}
			
			Spacer(modifier = Modifier.weight(1f))
			
			Button(onClick = onConnect) {
				Text("Connect")
			}
		}
	}
}
