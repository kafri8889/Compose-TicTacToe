package com.anafthdev.tictactoe.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.anafthdev.tictactoe.data.TicTacToeDestination
import com.anafthdev.tictactoe.ui.pvp_bluetooth.PvPBluetoothScreen
import com.anafthdev.tictactoe.ui.pvp_bluetooth.PvPBluetoothViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.PvPBluetoothAnimatedNavHost(navController: NavController) {
	navigation(
		startDestination = TicTacToeDestination.PvPBluetooth.Home.route,
		route = TicTacToeDestination.PvPBluetooth.Root.route
	) {
		composable(
			route = TicTacToeDestination.PvPBluetooth.Home.route,
			enterTransition = { fadeIn() },
			exitTransition = { fadeOut() },
			popEnterTransition = { fadeIn() },
			popExitTransition = { fadeOut() }
		) { backEntry ->
			val viewModel = hiltViewModel<PvPBluetoothViewModel>(backEntry)
			
			PvPBluetoothScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
