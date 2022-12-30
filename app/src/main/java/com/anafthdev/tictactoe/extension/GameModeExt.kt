package com.anafthdev.tictactoe.extension

import com.anafthdev.tictactoe.data.GameMode

val GameMode.modeName: String
	get() = when(this) {
		GameMode.Computer -> "Computer"
		GameMode.PvP -> "PvP"
		GameMode.PvPBluetooth -> "PvP Bluetooth"
	}
