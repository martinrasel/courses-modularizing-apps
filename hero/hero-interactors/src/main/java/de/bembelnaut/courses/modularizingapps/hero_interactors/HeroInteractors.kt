package de.bembelnaut.courses.modularizingapps.hero_interactors

import com.squareup.sqldelight.db.SqlDriver
import de.bembelnaut.courses.modularizingapps.hero_datasource.cache.HeroCache
import de.bembelnaut.courses.modularizingapps.hero_datasource.network.HeroService

data class HeroInteractors(
    val getHeros: GetHeros,
) {
    companion object Factory {
        fun build(sqlDriver: SqlDriver): HeroInteractors {
            return HeroInteractors(
                getHeros = GetHeros(
                    cache = HeroCache.build(
                        sqlDriver = sqlDriver
                    ),
                    service = HeroService.build(),
                )
            )
        }

        val schema: SqlDriver.Schema = HeroCache.schema

        val dbName: String = HeroCache.dbName
    }
}
