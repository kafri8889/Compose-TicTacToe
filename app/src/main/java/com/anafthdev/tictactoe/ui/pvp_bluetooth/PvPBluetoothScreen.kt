package com.anafthdev.tictactoe.ui.pvp_bluetooth

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.tictactoe.extension.toast
import com.anafthdev.tictactoe.uicomponent.BluetoothDeviceItem

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PvPBluetoothScreen(
	navController: NavController,
	viewModel: PvPBluetoothViewModel
) {
	
	val context = LocalContext.current
	
	val pullRefreshState = rememberPullRefreshState(
		refreshing = viewModel.isRefreshing,
		onRefresh = {
			if (viewModel.isBluetoothEnabled()) {
				viewModel.startDiscovery()
				"Discovering...".toast(context)
			} else "Bluetooth is off!".toast(context)
		}
	)
	
//	LaunchedEffect(viewModel.availableBluetoothDevice) {
//		viewModel.availableBluetoothDevice.toList().toast(context)
//	}
	
	LaunchedEffect(Unit) {
		if (viewModel.isBluetoothEnabled()) {
			viewModel.startDiscovery()
			"Discovering...".toast(context)
		} else "Bluetooth is off!".toast(context)
	}
	
	Box(
		modifier = Modifier
			.systemBarsPadding()
			.fillMaxSize()
			.pullRefresh(pullRefreshState)
	) {
		LazyColumn(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
                .fillMaxSize()
		) {
			items(viewModel.availableBluetoothDevice) { bluetoothDevice ->
				BluetoothDeviceItem(
					device = bluetoothDevice,
					onConnect = {
//						val isPaired = viewModel.isPaired(bluetoothDevice)
//
//						if (!isPaired) {
//							"Device not paired".toast(context)
//						} else viewModel.connect(bluetoothDevice)
						
						viewModel.connect(bluetoothDevice)
					},
					modifier = Modifier
						.fillMaxWidth()
						.padding(8.dp)
				)
			}
			
			item {
				Row {
					Button(
						onClick = {
							viewModel.tryRead()
						}
					) {
						Text("Try read")
					}
					
					Spacer(modifier = Modifier.width(8.dp))
					
					Button(
						onClick = {
							viewModel.tryWrite()
						}
					) {
						Text("Try write")
					}
				}
			}
		}
		
		PullRefreshIndicator(
			refreshing = viewModel.isRefreshing,
			state = pullRefreshState,
			modifier = Modifier
				.align(Alignment.TopCenter)
		)
	}
}
