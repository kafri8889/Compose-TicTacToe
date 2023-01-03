package com.anafthdev.tictactoe.common.di

import android.content.Context
import com.anafthdev.tictactoe.common.BluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {
	
	@Provides
	@Singleton
	fun provideBluetoothController(
		@ApplicationContext context: Context
	): BluetoothController = BluetoothController(context)
	
}