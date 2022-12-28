package com.anafthdev.tictactoe.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.anafthdev.tictactoe.uicomponent.TicTacToeBoard

@Composable
fun GameScreen(
	navController: NavController,
	viewModel: GameViewModel
) {

	Column(
		modifier = Modifier
			.fillMaxSize()
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.weight(1f)
		) {
			TicTacToeBoard(
				board = viewModel.board,
				onClick = { row, col ->
					viewModel.updateBoard(row, col)
				},
				modifier = Modifier
					.fillMaxWidth()
					.aspectRatio(1f / 1f)
			)
		}
	}
}
