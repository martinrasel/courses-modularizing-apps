package de.bembelnaut.courses.modularizingapps.hero_interactors

import de.bembelnaut.courses.modularizingapps.hero_datasource.network.HeroService

data class HeroInteractors(
    val getHeros: GetHeros,
) {
    companion object Factory {
        fun build(): HeroInteractors {
            return HeroInteractors(
                getHeros = GetHeros(
                    service = HeroService.build(),
                )
            )
        }
    }
}
