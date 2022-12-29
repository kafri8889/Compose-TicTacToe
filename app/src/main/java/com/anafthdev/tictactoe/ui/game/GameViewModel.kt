package com.anafthdev.tictactoe.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.tictactoe.common.GameEngine
import com.anafthdev.tictactoe.data.PointType
import com.anafthdev.tictactoe.data.TurnType
import com.anafthdev.tictactoe.data.WinType
import com.anafthdev.tictactoe.extension.to2DArray
import com.anafthdev.tictactoe.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(): ViewModel() {
	
	val board = mutableStateListOf<List<PointType>>()
	
	var currentTurn by mutableStateOf(TurnType.PlayerOne)
	private set
	
	var playerOne by mutableStateOf(Player.Player1)
	private set
	
	var playerTwo by mutableStateOf(Player.Player2)
	private set
	
	var winner by mutableStateOf(WinType.None)
	private set
	
	var round by mutableStateOf(1)
	private set
	
	var draw by mutableStateOf(0)
	private set
	
	private val gameEngine = GameEngine(
		playerOne = playerOne,
		playerTwo = playerTwo
	)
	
	init {
		gameEngine.setListener(object : GameEngine.Listener {
			override fun onWin(type: WinType) {
				winner = type
				
				when {
					playerOne.pointType == PointType.O && winner == WinType.O -> {
						playerOne = playerOne.copy(win = playerOne.win + 1)
					}
					playerOne.pointType == PointType.X && winner == WinType.X -> {
						playerOne = playerOne.copy(win = playerOne.win + 1)
					}
					playerTwo.pointType == PointType.O && winner == WinType.O -> {
						playerTwo = playerTwo.copy(win = playerTwo.win + 1)
					}
					playerTwo.pointType == PointType.X && winner == WinType.X -> {
						playerTwo = playerTwo.copy(win = playerTwo.win + 1)
					}
					winner == WinType.Tie -> {
						draw += 1
					}
				}
			}
		})
		
		viewModelScope.launch {
			gameEngine.board.collect { mBoard ->
				board.apply {
					clear()
					addAll(mBoard.to2DArray(3))
				}
			}
		}
		
		viewModelScope.launch {
			gameEngine.currentTurn.collect { turn ->
				currentTurn = turn
			}
		}
	}
	
	fun updateBoard(row: Int, col: Int) {
		val index = board[0].size * row + col
		
		viewModelScope.launch {
			gameEngine.updateBoard(index)
		}
	}
	
	fun clearBoard() {
		winner = WinType.None
		
		viewModelScope.launch {
			gameEngine.clearBoard()
		}
	}

}