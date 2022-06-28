package de.bembelnaut.courses.modularizingapps.ui_herodetail.ui

import de.bembelnaut.courses.modularizingapps.core.domain.ProgressBarState
import de.bembelnaut.courses.modularizingapps.core.domain.Queue
import de.bembelnaut.courses.modularizingapps.core.domain.UIComponent
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero

data class HeroDetailState(
    val progressBarState: ProgressBarState,
    val hero: Hero? = null,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
)
