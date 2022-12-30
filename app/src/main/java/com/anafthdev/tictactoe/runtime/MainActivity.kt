package com.anafthdev.tictactoe.runtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.anafthdev.tictactoe.runtime.navigation.TicTacToeNavigation
import com.anafthdev.tictactoe.theme.TicTacToeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		WindowCompat.setDecorFitsSystemWindows(window, false)
		
		setContent {
			TicTacToeTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					TicTacToeNavigation()
				}
			}
		}
	}
	
}
