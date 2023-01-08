package com.anafthdev.tictactoe.extension

import com.anafthdev.tictactoe.data.PointType
import com.anafthdev.tictactoe.data.WinType

fun PointType.toWinType(): WinType {
	return when (this) {
		PointType.Empty -> WinType.None
		PointType.X -> WinType.X
		PointType.O -> WinType.O
	}
}