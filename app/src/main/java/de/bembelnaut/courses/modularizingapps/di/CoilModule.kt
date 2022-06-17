package de.bembelnaut.courses.modularizingapps.di

import android.app.Application
import coil.ImageLoader
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.bembelnaut.courses.modularizingapps.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoilModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        applicationContext: Application,
    ) = ImageLoader.Builder(applicationContext)
        .error(R.drawable.error_image)
        .placeholder(R.drawable.white_background)
        .memoryCache { MemoryCache.Builder(applicationContext).maxSizePercent(.25).build() }
        .crossfade(true)
        .build()


}