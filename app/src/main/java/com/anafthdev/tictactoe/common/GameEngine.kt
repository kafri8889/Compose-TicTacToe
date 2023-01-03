package com.anafthdev.tictactoe.common

import com.anafthdev.tictactoe.data.GameMode
import com.anafthdev.tictactoe.data.PointType
import com.anafthdev.tictactoe.data.TurnType
import com.anafthdev.tictactoe.data.WinType
import com.anafthdev.tictactoe.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

/**
 * Board
 * 0|1|2
 * 3|4|5
 * 6|7|8
 */
class GameEngine(
	private var playerOne: Player,
	private var playerTwo: Player,
) {
	
	private val defaultBoard = ArrayList<PointType>(9).apply {
		repeat(9) { i ->
			add(i, PointType.Empty)
		}
	}
	
	private val _board = MutableStateFlow(defaultBoard)
	val board: StateFlow<ArrayList<PointType>> = _board.asStateFlow()
	
	private val _currentTurn = MutableStateFlow(TurnType.PlayerOne)
	val currentTurn: StateFlow<TurnType> = _currentTurn.asStateFlow()
	
	private var gameMode: GameMode = GameMode.Computer
	private var listener: Listener? = null
	
	private fun checkHorizontal(board: List<PointType>): WinType {
		for (i in 0 until 3) {
			when {
				board[i * 3] == PointType.O && board[(i * 3) + 1] == PointType.O && board[(i * 3) + 2] == PointType.O -> {
					return WinType.O
				}
				board[i * 3] == PointType.X && board[(i * 3) + 1] == PointType.X && board[(i * 3) + 2] == PointType.X -> {
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
	
	private fun checkWin(board: List<PointType>): WinType {
		val wins = listOf(checkHorizontal(board), checkVertical(board), checkDiagonal(board))
		Timber.i("fingsbuk: kolll")
		val isTie = wins.all { it == WinType.None } and board.all { it != PointType.Empty }
		
		val winner = when {
			isTie -> WinType.Tie
			WinType.O in wins -> WinType.O
			WinType.X in wins -> WinType.X
			else -> WinType.None
		}
		
		listener?.onWin(winner)
		
		return winner
	}
	
	suspend fun updateBoard(index: Int) {
		val newPointType = if (currentTurn.value == TurnType.PlayerOne) playerOne.pointType else playerTwo.pointType
		val newBoard = ArrayList(board.value).apply {
			set(index, newPointType)
		}
		
		_board.emit(newBoard)
		
		val winner = checkWin(newBoard)
		
		val nextTurn = if (currentTurn.value == TurnType.PlayerOne) TurnType.PlayerTwo else TurnType.PlayerOne
		
		_currentTurn.emit(nextTurn)
		
		if (winner == WinType.None) {
			when (nextTurn) {
				TurnType.PlayerOne -> {
					if (playerOne.id == Player.Computer.id) {
						computerTurn(newBoard)
					}
				}
				TurnType.PlayerTwo -> {
					if (playerTwo.id == Player.Computer.id) {
						computerTurn(newBoard)
					}
				}
			}
		}
	}
	
	private suspend fun computerTurn(board: List<PointType>) {
		val emptyIndex = arrayListOf<Int>()
		
		board.forEachIndexed { i, type ->
			if (type == PointType.Empty) emptyIndex.add(i)
		}
		
		if (emptyIndex.isNotEmpty()) {
			val randomIndex = emptyIndex.random()
			
			updateBoard(randomIndex)
		}
	}
	
	suspend fun clearBoard() {
		_board.emit(defaultBoard)
		
		when (currentTurn.value) {
			TurnType.PlayerOne -> {
				if (playerOne.id == Player.Computer.id) {
					computerTurn(defaultBoard)
				}
			}
			TurnType.PlayerTwo -> {
				if (playerTwo.id == Player.Computer.id) {
					computerTurn(defaultBoard)
				}
			}
		}
	}
	
	fun updatePlayer(one: Player, two: Player) {
		playerOne = one
		playerTwo = two
	}
	
	fun setListener(l: Listener) {
		listener = l
	}
	
	interface Listener {
		
		fun onWin(type: WinType)
	}

}