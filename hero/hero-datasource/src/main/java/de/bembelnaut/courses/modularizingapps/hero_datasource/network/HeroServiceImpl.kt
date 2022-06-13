package de.bembelnaut.courses.modularizingapps.hero_datasource.network

import de.bembelnaut.courses.modularizingapps.hero_datasource.network.model.EndPoints
import de.bembelnaut.courses.modularizingapps.hero_datasource.network.model.HeroDto
import de.bembelnaut.courses.modularizingapps.hero_datasource.network.model.toHero
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero
import io.ktor.client.*
import io.ktor.client.request.*

class HeroServiceImpl(
    private val httpClient: HttpClient,
): HeroService {
    override suspend fun getHeroStats(): List<Hero> =
        httpClient.get<List<HeroDto>> {
            url(EndPoints.HERO_STATS)
        }.map { it.toHero() }
}