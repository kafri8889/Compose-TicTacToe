package com.anafthdev.tictactoe.model

import com.anafthdev.tictactoe.data.PointType

data class Player(
	val id: Int,
	val win: Int,
	val name: String,
	var pointType: PointType
) {
	companion object {
		val Player1 = Player(
			id = 0,
			win = 0,
			name = "Player 1",
			pointType = PointType.X
		)
		
		val Player2 = Player(
			id = 1,
			win = 0,
			name = "Player 2",
			pointType = PointType.O
		)
		
		val Computer = Player(
			id = 2,
			win = 0,
			name = "Computer",
			pointType = PointType.O
		)
	}
}
