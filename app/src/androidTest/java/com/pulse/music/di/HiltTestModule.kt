package com.pulse.music.di

import com.pulse.music.database.dao.*
import com.pulse.music.player.MusicPlayer
import com.pulse.music.player.QueueManager
import com.pulse.music.player.ShuffleManager
import com.pulse.music.player.RepeatManager
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object HiltTestModule {

    @Provides
    @Singleton
    fun provideMusicPlayer(): MusicPlayer = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideQueueManager(): QueueManager = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideShuffleManager(): ShuffleManager = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideRepeatManager(): RepeatManager = mockk(relaxed = true)
}