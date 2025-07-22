package dev.diegoflassa.fuzecsgomatches.details.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.diegoflassa.fuzecsgomatches.details.data.network.PandaOpponentsApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModuleDetails {

    @Provides
    @Singleton
    fun providePandaOpponentsApiService(retrofit: Retrofit): PandaOpponentsApiService {
        return retrofit.create(PandaOpponentsApiService::class.java)
    }
}

