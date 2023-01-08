package com.anafthdev.tictactoe.common

import com.anafthdev.tictactoe.data.PointType
import com.anafthdev.tictactoe.extension.toWinType
import com.anafthdev.tictactoe.model.Player
import kotlin.math.abs


class Minimax(private val gameEngine: GameEngine) {
	
	private var maxDepth = 24

	private fun minimax(
		board: MutableList<PointType>,
		computerPlayer: Player,
		humanPlayer: Player,
		depth: Int,
		isMax: Boolean
	): Int {

		val winner = gameEngine.checkWin(board, false)

		val score = if (humanPlayer.pointType.toWinType() == winner) {
			-1
		} else if (computerPlayer.pointType.toWinType() == winner) {
			1
		} else 0
		
		if (abs(score) == 1 || depth == 0 || board.none { it == PointType.Empty }) {
			return score
		}
		
		return if (isMax) {
			var highestVal = Int.MIN_VALUE
			board.forEachIndexed { index, pointType ->
				if (pointType == PointType.Empty) {
					board[index] = computerPlayer.pointType
					
					highestVal = highestVal.coerceAtLeast(
						minimax(
							board,
							computerPlayer,
							humanPlayer,
							depth - 1,
							false
						)
					)
				}
			}
			
			highestVal
			// Minimising player, find the minimum attainable value;
		} else {
			var lowestVal = Int.MAX_VALUE
			board.forEachIndexed { index, pointType ->
				if (pointType == PointType.Empty) {
					board[index] = humanPlayer.pointType
					
					lowestVal = lowestVal.coerceAtMost(
						minimax(
							board,
							computerPlayer,
							humanPlayer,
							depth - 1,
							true
						)
					)
				}
			}
			
			lowestVal
		}
	}
	
	fun getBestMove(
		board: List<PointType>,
		computerPlayer: Player,
		humanPlayer: Player,
	): Int {
		var index = -1
		var bestValue = Int.MIN_VALUE
		
		val mutableBoard = board.toMutableList()
		
		board.forEachIndexed { i, type ->
			if (type == PointType.Empty) {
				mutableBoard[i] = computerPlayer.pointType
				
				val moveValue = minimax(mutableBoard, computerPlayer, humanPlayer, maxDepth, false)
				
				if (moveValue > bestValue) {
					index = i
                    bestValue = moveValue
				}
			}
		}
		
		return index
	}
	
}