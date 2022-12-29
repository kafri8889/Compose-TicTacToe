package com.anafthdev.tictactoe.uicomponent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.anafthdev.tictactoe.R
import com.anafthdev.tictactoe.data.PointType

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TicTacToeBoard(
	board: List<List<PointType>>,
	modifier: Modifier = Modifier,
	onClick: (row: Int, col: Int) -> Unit
) {
	
	BoxWithConstraints(modifier = modifier) {
		val dividerWidth = 8.dp
		val tileSize = (maxWidth / 3) - dividerWidth / 1.5f
		
		for ((row, pointTypeRow) in board.withIndex()) {
			for ((col, pointType) in pointTypeRow.withIndex()) {
				val endPadding = remember {
					when {
						col != 0 -> dividerWidth
						else -> 0.dp
					}
				}
				
				val bottomPadding = remember {
					when {
						row != 0 -> dividerWidth
						else -> 0.dp
					}
				}
				
				Box(
					modifier = Modifier
						.zIndex(1f)
						.offset(
							x = (tileSize * col) + (endPadding * col),
							y = (tileSize * row) + (bottomPadding * row)
						)
						.size(tileSize)
						.clickable {
							if (pointType == PointType.Empty) {
								onClick(row, col)
							}
						}
				) {
					AnimatedVisibility(
						visible = pointType != PointType.Empty,
						enter = scaleIn(
							animationSpec = tween(200)
						),
						exit = scaleOut(
							animationSpec = tween(200)
						),
						modifier = Modifier
							.padding(8.dp)
							.matchParentSize()
					) {
						Image(
							painter = painterResource(
								id = when (pointType) {
									PointType.Empty -> R.drawable.transparent
									PointType.X -> R.drawable.ic_tic_tac_toe_x
									PointType.O -> R.drawable.ic_tic_tac_toe_o
								}
							),
							contentDescription = null,
							modifier = Modifier
								.matchParentSize()
						)
					}
				}
			}
		}
		
		for (i in 0 until 2) {
			val padding = remember {
				when {
					i != 0 -> dividerWidth
					else -> 0.dp
				}
			}
			
			Divider(
				thickness = dividerWidth,
				modifier = Modifier
					.offset(
						x = tileSize * (i + 1) + padding,
						y = 0.dp
					)
					.size(dividerWidth, maxHeight)
					.clip(CircleShape)
			)

			Divider(
				thickness = dividerWidth,
				modifier = Modifier
					.offset(
						x = 0.dp,
						y = tileSize * (i + 1) + padding
					)
					.fillMaxWidth()
					.height(dividerWidth)
					.clip(CircleShape)
			)
		}
	}
}
