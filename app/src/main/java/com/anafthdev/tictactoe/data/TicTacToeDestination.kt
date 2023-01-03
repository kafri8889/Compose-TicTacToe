package com.anafthdev.tictactoe.data

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class TicTacToeDestination(val route: String) {

	class Dashboard {
		object Root: TicTacToeDestination("dashboard/root")
		object Home: TicTacToeDestination("dashboard/home")
	}
	
	class Score {
		object Root: TicTacToeDestination("score/root")
		object Home: TicTacToeDestination("score/home")
	}
	
	class PvPBluetooth {
		object Root: TicTacToeDestination("pvp_bluetooth/root")
		object Home: TicTacToeDestination("pvp_bluetooth/home")
	}
	
	class Game {
		object Root: TicTacToeDestination("game/root")
		object Home: TicTacToeDestination(
			route = "game/home?" +
					"$ARG_GAME_MODE={$ARG_GAME_MODE}"
		) {
			fun createRoute(gameMode: GameMode): String {
				return "game/home?" +
						"$ARG_GAME_MODE=${gameMode.ordinal}"
			}
			
			val arguments = listOf(
				navArgument(ARG_GAME_MODE) {
					type = NavType.IntType
				}
			)
		}
	}

}

const val ARG_GAME_MODE = "game_mode"
