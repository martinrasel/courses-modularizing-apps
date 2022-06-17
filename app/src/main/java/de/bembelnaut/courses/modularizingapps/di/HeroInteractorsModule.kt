package de.bembelnaut.courses.modularizingapps.di

import android.app.Application
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.bembelnaut.courses.modularizingapps.hero_interactors.HeroInteractors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroInteractorsModule {

    @Provides
    @Singleton
    fun provideAndroidDriver(app: Application): SqlDriver =
        AndroidSqliteDriver(
            schema = HeroInteractors.schema,
            context = app,
            name = HeroInteractors.dbName,
        )

    @Provides
    @Singleton
    fun provideHeroInteractors(
        sqlDriver: SqlDriver,
    ) = HeroInteractors.build(sqlDriver = sqlDriver)
}