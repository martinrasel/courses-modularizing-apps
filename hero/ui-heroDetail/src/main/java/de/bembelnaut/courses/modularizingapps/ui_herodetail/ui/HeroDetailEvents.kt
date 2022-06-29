package de.bembelnaut.courses.modularizingapps.ui_herodetail.ui

sealed class HeroDetailEvents {

    data class GetHeroFromCache(
        val id: Int,
    ) : HeroDetailEvents()

    object OnRemoveHeadFromQueue: HeroDetailEvents()
}
