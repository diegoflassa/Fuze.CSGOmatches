package dev.diegoflassa.fuzecsgomatches.details.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.diegoflassa.fuzecsgomatches.details.data.network.PandaOpponentsApiService
import dev.diegoflassa.fuzecsgomatches.details.data.repository.OpponentsRepository
import dev.diegoflassa.fuzecsgomatches.details.data.repository.interfaces.IOpponentsRepository

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModuleDetails {

    @Provides
    fun provideMatchesRepository(
        pandaMatchesApiService: PandaOpponentsApiService
    ): IOpponentsRepository {
        return OpponentsRepository(pandaMatchesApiService)
    }
}
