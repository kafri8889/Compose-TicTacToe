package com.anafthdev.tictactoe.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.anafthdev.tictactoe.data.TicTacToeDestination
import com.anafthdev.tictactoe.ui.dashboard.DashboardScreen
import com.anafthdev.tictactoe.ui.dashboard.DashboardViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.DashboardAnimatedNavHost(navController: NavController) {
	navigation(
		startDestination = TicTacToeDestination.Dashboard.Home.route,
		route = TicTacToeDestination.Dashboard.Root.route
	) {
		composable(
			route = TicTacToeDestination.Dashboard.Home.route,
			enterTransition = { fadeIn() },
			exitTransition = { fadeOut() },
			popEnterTransition = { fadeIn() },
			popExitTransition = { fadeOut() }
		) { backEntry ->
			val viewModel = hiltViewModel<DashboardViewModel>(backEntry)
			
			DashboardScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}

