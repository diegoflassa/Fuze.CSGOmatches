package dev.diegoflassa.fusecsgomatches.main.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.diegoflassa.fusecsgomatches.main.data.network.PandaMatchesApiService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModuleMain {

    @Provides
    @Singleton
    fun providePandaMatchesApiService(retrofit: Retrofit): PandaMatchesApiService {
        return retrofit.create(PandaMatchesApiService::class.java)
    }
}

