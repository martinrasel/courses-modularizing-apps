package de.bembelnaut.courses.modularizingapps.ui_herolist.ui

import de.bembelnaut.courses.modularizingapps.core.domain.*
import de.bembelnaut.courses.modularizingapps.hero_domain.Hero
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroAttribute
import de.bembelnaut.courses.modularizingapps.hero_domain.HeroFilter

data class HeroListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heros: List<Hero> = listOf(),
    val filteredHeros: List<Hero> = listOf(),
    val heroName: String = "",
    val heroFilter: HeroFilter = HeroFilter.Hero(FilterOrder.Descending),
    val primaryAttrFilter: HeroAttribute = HeroAttribute.Unknown,
    val filterDialogState: UIComponentState = UIComponentState.Hide, // show/hide the filter dialog
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf()),
)
