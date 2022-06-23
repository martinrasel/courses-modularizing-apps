package de.bembelnaut.courses.modularizingapps.hero_interactors

import de.bembelnaut.courses.modularizingapps.core.domain.DataState
import de.bembelnaut.courses.modularizingapps.core.domain.ProgressBarState
import de.bembelnaut.courses.modularizingapps.core.domain.UIComponent
import de.bembelnaut.courses.modularizingapps.hero_datasource.cache.HeroCache
import de.bembelnaut.courses.modularizingapps.hero_datasource.network.HeroService
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHeros(
    private val cache: HeroCache,
    private val service: HeroService,
) {

    fun execute(): Flow<DataState<List<Hero>>> = flow {
        try {
            emit(DataState.Loading(progressBarState = ProgressBarState.Loading))

            val heros: List<Hero> = try {
                service.getHeroStats()
            } catch (e: Exception) {
                e.printStackTrace()
                // log to crashlytics?
                emit(
                    DataState.Response<List<Hero>>(
                        uiComponent = UIComponent.Dialog(
                            title = "Error",
                            description = e.message ?: "Unknown error"
                        )
                    )
                )
                listOf()
            }

            // cache the network data
            cache.insert(heros)

            // emit data from cache
            val cachedHeros = cache.selectAll()

            emit(DataState.Data(cachedHeros))
        } catch (e: Exception) {
            e.printStackTrace()
            // log to crashlytics?
            emit(
                DataState.Response<List<Hero>>(
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