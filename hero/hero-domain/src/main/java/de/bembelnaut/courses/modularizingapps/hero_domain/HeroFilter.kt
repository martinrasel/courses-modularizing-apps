package de.bembelnaut.courses.modularizingapps.hero_domain

import de.bembelnaut.courses.modularizingapps.core.domain.FilterOrder

sealed class HeroFilter(
    val uiValue: String,
) {

    data class Hero(
        val order: FilterOrder = FilterOrder.Descending
    ) : HeroFilter("Hero")

    data class ProWins(
        val order: FilterOrder = FilterOrder.Descending
    ) : HeroFilter("Pro win-rate")

}