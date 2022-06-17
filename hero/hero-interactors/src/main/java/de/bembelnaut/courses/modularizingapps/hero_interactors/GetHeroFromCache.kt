package de.bembelnaut.courses.modularizingapps.hero_interactors

import de.bembelnaut.courses.modularizingapps.core.DataState
import de.bembelnaut.courses.modularizingapps.core.ProgressBarState
import de.bembelnaut.courses.modularizingapps.core.UIComponent
import de.bembelnaut.courses.modularizingapps.hero_datasource.cache.HeroCache
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHeroFromCache(
    private val cache: HeroCache,
) {

    fun execute(
        id: Int
    ): Flow<DataState<Hero>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val cacheHero = cache.getHero(id)

            cacheHero?.let {
                emit(DataState.Data(it))
            } ?: throw Exception("That hero does not exists in the cache!")

        } catch (e: Exception) {
            e.printStackTrace()
            // log to crashlytics?
            emit(
                DataState.Response<Hero>(
                    uiComponent = UIComponent.Dialog(
                        title = "Error",
                        description = e.message ?: "Unknown error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(progressBarState = ProgressBarState.Idle))
        }
    }
}