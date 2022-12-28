package com.anafthdev.tictactoe.ui.game

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.tictactoe.common.GameEngine
import com.anafthdev.tictactoe.data.PointType
import com.anafthdev.tictactoe.data.WinType
import com.anafthdev.tictactoe.extension.to2DArray
import com.anafthdev.tictactoe.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(): ViewModel() {

	private val gameEngine = GameEngine(
		playerOne = Player.Player1,
		playerTwo = Player.Player2
	)
	
	val board = mutableStateListOf<List<PointType>>()
	
	init {
		gameEngine.setListener(object : GameEngine.Listener {
			override fun onWin(type: WinType) {
				when (type) {
					WinType.None -> {}
					WinType.Tie -> {}
					WinType.O -> {}
					WinType.X -> {}
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
	}
	
	fun updateBoard(row: Int, col: Int) {
		val index = board[0].size * row + col
		
		viewModelScope.launch {
			gameEngine.updateBoard(index)
		}
	}

}