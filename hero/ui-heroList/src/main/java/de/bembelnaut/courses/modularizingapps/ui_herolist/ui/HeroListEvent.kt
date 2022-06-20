package de.bembelnaut.courses.modularizingapps.ui_herolist.ui

sealed class HeroListEvent {

    object GetHeros : HeroListEvent()

    object FilterHeros : HeroListEvent()

    data class UpdateHeroName(
        val heroName: String,
    ) : HeroListEvent()
}
