package com.anafthdev.tictactoe.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun ColumnScope.Padding(
	all: Dp,
	content: @Composable ColumnScope.() -> Unit
) {
	Column(
		modifier = Modifier
			.padding(all)
	) {
		content()
	}
}

