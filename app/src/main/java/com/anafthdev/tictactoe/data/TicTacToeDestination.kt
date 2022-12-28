package com.anafthdev.tictactoe.data

sealed class TicTacToeDestination(val route: String) {

	class Dashboard {
		object Root: TicTacToeDestination("dashboard/root")
		object Home: TicTacToeDestination("dashboard/home")
	}
	
	class Score {
		object Root: TicTacToeDestination("score/root")
		object Home: TicTacToeDestination("score/home")
	}
	
	class Game {
		object Root: TicTacToeDestination("game/root")
		object Home: TicTacToeDestination("game/home")
	}

}