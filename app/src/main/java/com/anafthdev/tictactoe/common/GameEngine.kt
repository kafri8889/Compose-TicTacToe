package com.anafthdev.tictactoe.common

import com.anafthdev.tictactoe.data.PointType
import com.anafthdev.tictactoe.data.TurnType
import com.anafthdev.tictactoe.data.WinType
import com.anafthdev.tictactoe.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Board
 * 0|1|2
 * 3|4|5
 * 6|7|8
 */
class GameEngine(
	val playerOne: Player,
	val playerTwo: Player,
) {
	
	private val defaultBoard = ArrayList<PointType>(9).apply {
		repeat(9) { i ->
			add(i, PointType.Empty)
		}
	}
	
	private val _board = MutableStateFlow(defaultBoard)
	val board: StateFlow<ArrayList<PointType>> = _board.asStateFlow()
	
	private var listener: Listener? = null
	
	private var currentTurn = TurnType.PlayerOne
	
	private fun checkHorizontal(board: List<PointType>): WinType {
		for (i in 0 until 3) {
			when {
				board[i] == PointType.O && board[i + 3] == PointType.O && board[i + 6] == PointType.O -> {
					return WinType.O
				}
				board[i] == PointType.X && board[i + 3] == PointType.X && board[i + 6] == PointType.X -> {
					return WinType.X
				}
			}
		}
		
		return WinType.None
	}
	
	private fun checkVertical(board: List<PointType>): WinType {
		for (i in 0 until 3) {
			when {
				board[i] == PointType.O && board[i + 3] == PointType.O && board[i + 6] == PointType.O -> {
					return WinType.O
				}
				board[i] == PointType.X && board[i + 3] == PointType.X && board[i + 6] == PointType.X -> {
					return WinType.X
				}
			}
		}
		
		return WinType.None
	}
	
	private fun checkDiagonal(board: List<PointType>): WinType {
		when {
			board[0] == PointType.O && board[4] == PointType.O && board[8] == PointType.O -> {
				return WinType.O
			}
			board[0] == PointType.X && board[4] == PointType.X && board[8] == PointType.X -> {
				return WinType.X
			}
			board[2] == PointType.O && board[4] == PointType.O && board[6] == PointType.O -> {
				return WinType.O
			}
			board[2] == PointType.X && board[4] == PointType.X && board[6] == PointType.X -> {
				return WinType.X
			}
		}
		
		return WinType.None
	}
	
	private fun checkWin() {
		val board = board.value
		
		val wins = listOf(checkDiagonal(board), checkVertical(board), checkDiagonal(board))
		
		listener?.onWin(
			when {
				WinType.Tie in wins -> WinType.Tie
				WinType.O in wins -> WinType.O
				WinType.X in wins -> WinType.X
				else -> WinType.None
			}
		)
	}
	
	suspend fun updateBoard(index: Int) {
		val newPointType = if (currentTurn == TurnType.PlayerOne) playerOne.pointType else playerTwo.pointType
		
		_board.emit(
			ArrayList(board.value).apply {
				set(index, newPointType)
			}
		)
		
		checkWin()
		
		currentTurn = if (currentTurn == TurnType.PlayerOne) TurnType.PlayerTwo else TurnType.PlayerOne
	}
	
	fun setListener(l: Listener) {
		listener = l
	}
	
	interface Listener {
		
		fun onWin(type: WinType)
	}

}