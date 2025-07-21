package dev.diegoflassa.fusecsgomatches.details.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.diegoflassa.fusecsgomatches.details.domain.useCases.GetOpponentsUseCase
import dev.diegoflassa.fusecsgomatches.details.domain.useCases.IGetOpponentsUseCase

@Module
@InstallIn(ViewModelComponent::class)
abstract class DetailsDomainModule {

    @Binds
    abstract fun bindGetOpponentsUseCase(
        getOpponentsUseCaseImpl: GetOpponentsUseCase
    ): IGetOpponentsUseCase
}
