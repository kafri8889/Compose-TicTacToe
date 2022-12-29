package com.anafthdev.tictactoe.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.tictactoe.extension.toast
import com.anafthdev.tictactoe.uicomponent.PlayerItem
import com.anafthdev.tictactoe.uicomponent.RoundItem
import com.anafthdev.tictactoe.uicomponent.TicTacToeBoard

@Composable
fun GameScreen(
	navController: NavController,
	viewModel: GameViewModel
) {
	
	val context = LocalContext.current
	
	LaunchedEffect(viewModel.winner) {
		viewModel.winner.toast(context)
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.surface)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth()
		) {
			PlayerItem(
				player = viewModel.playerOne
			)
			
			RoundItem(
				round = viewModel.round,
				draw = viewModel.draw
			)
			
			PlayerItem(
				player = viewModel.playerTwo
			)
		}
		
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
