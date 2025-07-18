package dev.diegoflassa.fusecsgomatches.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.diegoflassa.fusecsgomatches.core.data.config.Config
import dev.diegoflassa.fusecsgomatches.core.data.config.IConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConfigModule {

    @Singleton
    @Provides
    fun provideConfig(@ApplicationContext context: Context): IConfig {
        return Config(context)
    }
}
