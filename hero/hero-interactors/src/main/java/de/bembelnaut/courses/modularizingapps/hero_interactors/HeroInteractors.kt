package de.bembelnaut.courses.modularizingapps.hero_interactors

import com.squareup.sqldelight.db.SqlDriver
import de.bembelnaut.courses.modularizingapps.hero_datasource.cache.HeroCache
import de.bembelnaut.courses.modularizingapps.hero_datasource.network.HeroService

data class HeroInteractors(
    val getHeros: GetHeros,
    val getHeroFromCache: GetHeroFromCache,
    val filterHeros: FilterHeros,
) {
    companion object Factory {
        fun build(sqlDriver: SqlDriver): HeroInteractors {
            val cache = HeroCache.build(
                sqlDriver = sqlDriver
            )

            return HeroInteractors(
                getHeros = GetHeros(
                    cache = cache,
                    service = HeroService.build(),
                ),
                getHeroFromCache = GetHeroFromCache(
                    cache = cache
                ),
                filterHeros = FilterHeros(),
            )
        }

        val schema: SqlDriver.Schema = HeroCache.schema

        val dbName: String = HeroCache.dbName
    }
}
