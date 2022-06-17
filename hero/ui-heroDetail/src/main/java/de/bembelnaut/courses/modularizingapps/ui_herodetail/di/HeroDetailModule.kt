package de.bembelnaut.courses.modularizingapps.ui_herodetail.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.bembelnaut.courses.modularizingapps.hero_interactors.HeroInteractors
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HeroDetailModule {

    @Provides
    @Singleton
    fun provideGetHeroListFromCache(
        interactors: HeroInteractors
    ) = interactors.getHeroFromCache
}
