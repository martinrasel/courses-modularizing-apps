package de.bembelnaut.courses.modularizingapps.ui_herolist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.bembelnaut.courses.modularizingapps.hero_interactors.HeroInteractors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroListModule {

    @Provides
    @Singleton
    fun provideGetHeros(
        interactors: HeroInteractors
    ) = interactors.getHeros

}