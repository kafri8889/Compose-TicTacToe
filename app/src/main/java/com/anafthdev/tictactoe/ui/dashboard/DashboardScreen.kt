package com.anafthdev.tictactoe.ui.dashboard

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anafthdev.tictactoe.R
import com.anafthdev.tictactoe.data.TicTacToeDestination

@Composable
fun DashboardScreen(navController: NavController) {
	
	val context = LocalContext.current
	
	val openSourceProjectMsg = buildAnnotatedString {
		withStyle(
			style = LocalTextStyle.current.copy(
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Light
			).toSpanStyle()
		) {
			append("This is an open source project, source code can be found on ")
			
			pushStringAnnotation(tag = "github", annotation = "https://google.com/policy")
			
			withStyle(
				style = SpanStyle(
					color = MaterialTheme.colorScheme.primary,
					fontWeight = FontWeight.Medium
				)
			) {
				append("GitHub")
			}
			
			pop()
			
			append(" or by clicking on the GitHub icon above")
		}
	}
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
		modifier = Modifier
			.fillMaxSize()
	) {
		Image(
//			painter = painterResource(id = R.drawable.ic_app_icon),
			painter = ColorPainter(Color.Gray),
			contentDescription = null,
			modifier = Modifier
				.size(96.dp)
				.clip(MaterialTheme.shapes.large)
		)
		
		Spacer(modifier = Modifier.padding(8.dp))
		
		OutlinedButton(
			onClick = {
//				navController.navigate(TicTacToeDestination.Game.Home.createRoute())
			}
		) {
			Text("Play")
		}
		
		Spacer(modifier = Modifier.padding(16.dp))
		
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			IconButton(
				onClick = {
					navController.navigate(TicTacToeDestination.Score.Home.route)
				}
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_award),
					contentDescription = null
				)
			}
			
			IconButton(
				onClick = {
					context.startActivity(
						Intent(Intent.ACTION_VIEW).apply {
							flags = Intent.FLAG_ACTIVITY_NEW_TASK
							data = Uri.parse("https://github.com/kafri8889/Compose-Classic-Snake-Game")
						}
					)
				}
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_github_mark),
					contentDescription = null
				)
			}
		}
		
		Spacer(modifier = Modifier.padding(16.dp))
		
		Text(
			text = "Open Source",
			style = MaterialTheme.typography.titleLarge.copy(
				fontWeight = FontWeight.Light
			)
		)
		
		Spacer(modifier = Modifier.padding(8.dp))
		
		ClickableText(
			text = openSourceProjectMsg,
			style = LocalTextStyle.current.copy(
				textAlign = TextAlign.Center
			),
			onClick = { offset ->
				openSourceProjectMsg.getStringAnnotations(
					tag = "github",
					start = offset,
					end = offset
				).firstOrNull()?.let { _ ->
					context.startActivity(
						Intent(Intent.ACTION_VIEW).apply {
							flags = Intent.FLAG_ACTIVITY_NEW_TASK
							data = Uri.parse("https://github.com/kafri8889/Compose-Classic-Snake-Game")
						}
					)
				}
			},
			modifier = Modifier
				.fillMaxWidth(0.7f)
		)
	}
}
