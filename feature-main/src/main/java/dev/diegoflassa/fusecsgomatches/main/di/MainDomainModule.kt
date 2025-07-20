package dev.diegoflassa.fusecsgomatches.main.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dev.diegoflassa.fusecsgomatches.main.domain.useCases.GetMatchesUseCase
import dev.diegoflassa.fusecsgomatches.main.domain.useCases.IGetMatchesUseCase

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainDomainModule {

    @Binds
    abstract fun bindGetMatchesUseCase(
        getMatchesUseCaseImpl: GetMatchesUseCase
    ): IGetMatchesUseCase
}
