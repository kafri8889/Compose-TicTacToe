package com.anafthdev.tictactoe.runtime.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.anafthdev.tictactoe.data.TicTacToeDestination
import com.anafthdev.tictactoe.ui.game.GameScreen
import com.anafthdev.tictactoe.ui.game.GameViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.GameAnimatedNavHost(navController: NavController) {
	navigation(
		startDestination = TicTacToeDestination.Game.Home.route,
		route = TicTacToeDestination.Game.Root.route
	) {
		composable(
			route = TicTacToeDestination.Game.Home.route,
			enterTransition = { fadeIn() },
			exitTransition = { fadeOut() },
			popEnterTransition = { fadeIn() },
			popExitTransition = { fadeOut() }
		) { backEntry ->
			val viewModel = hiltViewModel<GameViewModel>(backEntry)
			
			GameScreen(
				navController = navController,
				viewModel = viewModel
			)
		}
	}
}
