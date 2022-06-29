package de.bembelnaut.courses.modularizingapps.hero_datasource_test.network

import de.bembelnaut.courses.modularizingapps.hero_datasource.network.model.HeroDto
import de.bembelnaut.courses.modularizingapps.hero_datasource.network.model.toHero
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
}

fun serializeHeroData(jsonData: String): List<Hero> {
    val heros: List<Hero> = json.decodeFromString<List<HeroDto>>(jsonData).map { it.toHero() }
    return heros
}