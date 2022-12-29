package com.anafthdev.tictactoe.uicomponent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun RoundItem(
	draw: Int,
	round: Int,
	modifier: Modifier = Modifier
) {
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
	) {
		Card(
			shape = RoundedCornerShape(100),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.tertiaryContainer
			)
		) {
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
				modifier = Modifier
					.padding(8.dp)
					.size(56.dp)
			) {
				Text(
					text = buildAnnotatedString {
						withStyle(
							MaterialTheme.typography.titleMedium.copy(
								fontWeight = FontWeight.ExtraBold
							).toSpanStyle()
						) {
							append("$round")
						}
						
						withStyle(
							MaterialTheme.typography.bodyMedium.copy(
								fontWeight = FontWeight.ExtraBold,
								baselineShift = BaselineShift.Superscript
							).toSpanStyle()
						) {
							append(
								when (round) {
									1 -> "st"
									2 -> "nd"
									3 -> "rd"
									else -> "th"
								}
							)
						}
					}
				)
				
				Text(
					text = "Round",
					style = MaterialTheme.typography.labelSmall
				)
			}
		}
		
		Spacer(modifier = Modifier.height(16.dp))
		
		Text(
			text = buildAnnotatedString {
				withStyle(
					MaterialTheme.typography.titleSmall.toSpanStyle()
				) {
					append("Draw: ")
				}
				
				withStyle(
					MaterialTheme.typography.titleSmall.copy(
						color = MaterialTheme.colorScheme.primary
					).toSpanStyle()
				) {
					append("$draw times")
				}
			}
		)
	}
}
