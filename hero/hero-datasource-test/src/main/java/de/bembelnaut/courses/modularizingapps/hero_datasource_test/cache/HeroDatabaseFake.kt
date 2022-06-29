package de.bembelnaut.courses.modularizingapps.hero_datasource_test.cache

import de.bembelnaut.courses.modularizingapps.hero_domain.Hero

class HeroDatabaseFake {

    val heros: MutableList<Hero> = mutableListOf()
}