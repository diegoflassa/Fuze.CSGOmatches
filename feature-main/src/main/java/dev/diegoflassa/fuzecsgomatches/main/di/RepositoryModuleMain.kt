package dev.diegoflassa.fuzecsgomatches.main.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.diegoflassa.fuzecsgomatches.main.data.network.PandaMatchesApiService
import dev.diegoflassa.fuzecsgomatches.main.data.repository.MatchesRepository
import dev.diegoflassa.fuzecsgomatches.main.data.repository.interfaces.IMatchesRepository

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModuleMain {

    @Provides
    fun provideMatchesRepository(
        pandaMatchesApiService: PandaMatchesApiService
    ): IMatchesRepository {
        return MatchesRepository(pandaMatchesApiService)
    }
}
