package com.anafthdev.tictactoe.extension

infix fun Int.countMod(with: Int): Int {
	var num = this
	var count = 0
	
	while (num >= with) {
		num -= with
		count++
	}
	
	return count
}
