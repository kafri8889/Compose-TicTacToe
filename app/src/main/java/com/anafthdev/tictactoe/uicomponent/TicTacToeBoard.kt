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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
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
	
	val dividerWidth = remember { 8.dp }
	
	BoxWithConstraints(modifier = modifier) {
		val tileSize = remember(maxWidth, dividerWidth) {
			(maxWidth / 3) - dividerWidth / 1.5f
		}
		
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
						PointTypeImage(
							pointType = pointType
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
			
			BoardDivider(
				maxHeight = maxHeight,
				dividerThickness = { dividerWidth },
				offsetVert = {
					IntOffset(
						x = (tileSize * (i + 1) + padding)
							.toPx()
							.toInt(),
						y = 0.dp
							.toPx()
							.toInt()
					)
				},
				offsetHorz = {
					IntOffset(
						x = 0.dp
							.toPx()
							.toInt(),
						y = (tileSize * (i + 1) + padding)
							.toPx()
							.toInt()
					)
				}
			)
		}
	}
}

@Composable
private fun BoardDivider(
	maxHeight: Dp,
	dividerThickness: () -> Dp,
	offsetHorz: Density.() -> IntOffset,
	offsetVert: Density.() -> IntOffset
) {
	
	Divider(
		thickness = dividerThickness(),
		modifier = Modifier
			.offset {
				offsetVert()
			}
			.size(dividerThickness(), maxHeight)
			.clip(CircleShape)
	)
	
	Divider(
		thickness = dividerThickness(),
		modifier = Modifier
			.offset {
				offsetHorz()
			}
			.fillMaxWidth()
			.height(dividerThickness())
			.clip(CircleShape)
	)
}

@Composable
private fun BoxScope.PointTypeImage(
	pointType: PointType
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
