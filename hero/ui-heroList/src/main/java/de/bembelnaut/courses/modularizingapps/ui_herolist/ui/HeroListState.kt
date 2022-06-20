package de.bembelnaut.courses.modularizingapps.ui_herolist.ui

import de.bembelnaut.courses.modularizingapps.core.ProgressBarState
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero

data class HeroListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heros: List<Hero> = listOf(),
    val filteredHeros: List<Hero> = listOf(),
    val heroName: String = "",
)
