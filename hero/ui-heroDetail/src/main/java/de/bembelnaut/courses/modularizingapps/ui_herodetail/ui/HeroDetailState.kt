package de.bembelnaut.courses.modularizingapps.ui_herodetail.ui

import de.bembelnaut.courses.modularizingapps.core.ProgressBarState
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero

data class HeroDetailState(
    val progressBarState: ProgressBarState,
    val hero: Hero? = null,
)
