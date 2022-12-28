package com.anafthdev.tictactoe.runtime

import android.app.Application
import com.anafthdev.tictactoe.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TicTacToeApplication: Application() {
	
	override fun onCreate() {
		super.onCreate()
		if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
	}
}