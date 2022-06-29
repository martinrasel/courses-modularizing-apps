package de.bembelnaut.courses.modularizingapps.hero_interactors

import de.bembelnaut.courses.modularizingapps.core.domain.DataState
import de.bembelnaut.courses.modularizingapps.core.domain.ProgressBarState
import de.bembelnaut.courses.modularizingapps.core.domain.UIComponent
import de.bembelnaut.courses.modularizingapps.hero_datasource_test.cache.HeroCacheFake
import de.bembelnaut.courses.modularizingapps.hero_datasource_test.cache.HeroDatabaseFake
import de.bembelnaut.courses.modularizingapps.hero_datasource_test.network.data.HeroDataValid
import de.bembelnaut.courses.modularizingapps.hero_datasource_test.network.serializeHeroData
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random

/**
 * 1. Success (Retrieve a hero from the cache successfully)
 * 2. Failure (The hero does not exist in the cache)
 */
class GetHeroFromCacheTest {

    // system in test
    private lateinit var getHeroFromCache: GetHeroFromCache

    @Test
    fun getHeroFromCache_success() = runBlocking {
        // setup
        val heroDatabase = HeroDatabaseFake()
        val heroCache = HeroCacheFake(heroDatabase)

        getHeroFromCache = GetHeroFromCache(heroCache)

        // insert heros into the cache
        val heroData = serializeHeroData(HeroDataValid.data)
        heroCache.insert(heroData)

        // choose a hero at random
        val hero = heroData.get(Random.nextInt(0, heroData.size - 1))

        // Execute the use-case
        val emissions = getHeroFromCache.execute(hero.id).toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading<Hero>(ProgressBarState.Loading))

        // Confirm second emission is data from the cache
        assert(emissions[1] is DataState.Data)
        assert((emissions[1] as DataState.Data).data?.id == hero.id)
        assert((emissions[1] as DataState.Data).data?.localizedName == hero.localizedName)

        // Confirm loading state is IDLE
        assert(emissions[2] == DataState.Loading<Hero>(ProgressBarState.Idle))
    }

    @Test
    fun getHeroFromCache_fail() = runBlocking {
        // setup
        val heroDatabase = HeroDatabaseFake()
        val heroCache = HeroCacheFake(heroDatabase)

        getHeroFromCache = GetHeroFromCache(heroCache)

        // insert heros into the cache
        val heroData = serializeHeroData(HeroDataValid.data)
        heroCache.insert(heroData)

        // choose a hero at random and remove it from the cache
        val hero = heroData.get(Random.nextInt(0, heroData.size - 1))
        heroCache.removeHero(hero.id)

        // Execute the use-case
        val emissions = getHeroFromCache.execute(hero.id).toList()

        // First emission should be loading
        assert(emissions[0] == DataState.Loading<Hero>(ProgressBarState.Loading))

        // Confirm second emission is error response
        assert(emissions[1] is DataState.Response)
        assert(((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).title == "Error")
        assert(
            ((emissions[1] as DataState.Response).uiComponent as UIComponent.Dialog).description.contains(
                "That hero does not exists in the cache!"
            )
        )

        // Confirm loading state is IDLE
        assert(emissions[2] == DataState.Loading<Hero>(ProgressBarState.Idle))
    }
}













